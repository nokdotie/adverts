package ie.nok.adverts.stores

import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.stores.compose.ZFileAndGoogleStorageStore
import ie.nok.stores.google.storage.StorageConvention
import java.time.{Instant}
import scala.util.chaining.scalaUtilChainingOps
import zio.stream.ZStream
import zio.{ZIO, ZLayer}

object AdvertStoreImpl {
  private val blobNameLatest: String =
    StorageConvention.blobNameLatest("adverts", ".jsonl")

  private def blobNameLatestForService(service: AdvertService): String =
    StorageConvention.blobNameLatest(s"adverts/${service.host}", ".jsonl")

  private def blobNameVersionedForService(service: AdvertService): String =
    StorageConvention.blobNameVersioned(s"adverts/${service.host}", Instant.now, ".jsonl")

  protected[adverts] def encodeAndWriteLatest[R](
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & ZFileAndGoogleStorageStore[Advert], Throwable, Unit] =
    ZFileAndGoogleStorageStore.write[R, Advert](
      StorageConvention.bucketName,
      blobNameLatest,
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
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[Any, Throwable, List[Advert]] =
    all
      .filter(filter.filter)
      .slice(after.index, after.index + first)
      .pipe { ZIO.succeed }

}
