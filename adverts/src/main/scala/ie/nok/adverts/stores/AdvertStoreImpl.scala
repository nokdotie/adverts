package ie.nok.adverts.stores

import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.unit.Direction
import ie.nok.stores.compose.ZFileAndGoogleStorageStore
import ie.nok.stores.google.storage.StorageConvention
import ie.nok.stores.pagination.{Page, PageInfo}
import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.math.Ordered.orderingToOrdered
import scala.util.chaining.scalaUtilChainingOps
import zio.stream.ZStream
import zio.{ZIO, ZLayer}

object AdvertStoreImpl {
  private val blobNameLatest: String =
    StorageConvention.blobNameLatest("adverts", ".jsonl")

  private val blobNameAggregatedPrefix = "adverts/aggregated"
  private val blobNameAggregatedLatest: String =
    StorageConvention.blobNameLatest(blobNameAggregatedPrefix, ".jsonl")

  private val blobNameAggregatedVersioned: String =
    StorageConvention.blobNameVersioned(blobNameAggregatedPrefix, Instant.now, ".jsonl")

  private def blobNameLatestForService(service: AdvertService): String =
    StorageConvention.blobNameLatest(s"adverts/${service.host}", ".jsonl")

  private def blobNameVersionedForService(service: AdvertService): String =
    StorageConvention.blobNameVersioned(s"adverts/${service.host}", Instant.now, ".jsonl")

  protected[adverts] def encodeAndWriteLatest[R](
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & ZFileAndGoogleStorageStore[Advert], Throwable, Unit] =
    ZFileAndGoogleStorageStore.write[R, Advert](
      List(
        (StorageConvention.bucketName, blobNameLatest),
        (StorageConvention.bucketName, blobNameAggregatedLatest),
        (StorageConvention.bucketName, blobNameAggregatedVersioned)
      ),
      stream
    )

  protected[adverts] def encodeAndWriteForService[R](
      service: AdvertService,
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & ZFileAndGoogleStorageStore[Advert], Throwable, Unit] =
    ZFileAndGoogleStorageStore.write[R, Advert](
      List(
        (StorageConvention.bucketName, blobNameLatestForService(service)),
        (StorageConvention.bucketName, blobNameVersionedForService(service))
      ),
      stream
    )

  private def readAndDecode(
      blobName: String
  ): ZIO[ZFileAndGoogleStorageStore[Advert], Throwable, List[Advert]] =
    ZFileAndGoogleStorageStore
      .read[Advert](StorageConvention.bucketName, blobName)
      .runCollect
      .map { _.toList }

  protected[adverts] val readAndDecodeYesterday: ZIO[ZFileAndGoogleStorageStore[Advert], Throwable, List[Advert]] = {
    val yesterday = Instant.now.minus(1, ChronoUnit.DAYS)
    val blobNameGlob = StorageConvention
      .blobNameVersionedGlobPatternForDay(blobNameAggregatedPrefix, yesterday, ".jsonl")

    ZFileAndGoogleStorageStore
      .listBlobNames[Advert](StorageConvention.bucketName, blobNameGlob)
      .runCollect
      .map { _.maxOption }
      .flatMap {
        case Some(blobName) => readAndDecode(blobName)
        case None           => ZIO.succeed(List.empty)
      }
  }

  protected[adverts] val readAndDecodeLatest: ZIO[ZFileAndGoogleStorageStore[Advert], Throwable, List[Advert]] =
    readAndDecode(blobNameLatest)

  protected[adverts] def readAndDecodeLatestForService(
      service: AdvertService
  ): ZIO[ZFileAndGoogleStorageStore[Advert], Throwable, List[Advert]] =
    readAndDecode(blobNameLatestForService(service))

  val live: ZLayer[ZFileAndGoogleStorageStore[Advert], Throwable, AdvertStore] =
    readAndDecodeLatest
      .map { new AdvertStoreImpl(_) }
      .pipe { ZLayer.fromZIO }
}

class AdvertStoreImpl(all: List[Advert]) extends AdvertStore {
  def getPage(
      filter: AdvertFilter,
      sort: AdvertSort,
      first: Int,
      after: AdvertCursor
  ): ZIO[Any, Throwable, Page[(Advert, AdvertCursor)]] = {
    val filteredAndSorted = all
      .filter(filter.filter)
      .sorted(sort.sort)
      .map { advert => (advert, AdvertCursor.from(advert, sort.field)) }

    val sliced = filteredAndSorted
      .dropWhile { advert =>
        (after, sort.direction) match {
          case (AdvertCursor.Empty, _)       => false
          case (after, Direction.Ascending)  => advert._2 <= after
          case (after, Direction.Descending) => advert._2 >= after
        }
      }
      .take(first)

    val page = Page(
      items = sliced,
      PageInfo(
        hasPreviousPage = filteredAndSorted.headOption != sliced.headOption,
        hasNextPage = filteredAndSorted.lastOption != sliced.lastOption
      )
    )

    ZIO.succeed(page)
  }

}
