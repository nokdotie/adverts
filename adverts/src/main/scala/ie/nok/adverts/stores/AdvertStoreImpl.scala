package ie.nok.adverts.stores

import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.file.ZFileService
import ie.nok.gcp.storage.{StorageConvention, ZStorageService}
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

  private def encodeAndWrite[R](
      stream: ZStream[R, Throwable, Advert],
      blobNames: List[String]
  ): ZIO[R & ZFileService[Advert] & ZStorageService, Throwable, Unit] = for {
    file <- ZFileService.write[R, Advert](stream)
    _ <- blobNames
      .map { blobName =>
        ZStorageService.write(StorageConvention.bucketName, blobName, file)
      }
      .pipe { ZIO.collectAll }
  } yield ()

  protected[adverts] def encodeAndWriteLatest[R](
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & ZFileService[Advert] & ZStorageService, Throwable, Unit] =
    encodeAndWrite(stream, List(blobNameLatest))

  protected[adverts] def encodeAndWriteForService[R](
      service: AdvertService,
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & ZFileService[Advert] & ZStorageService, Throwable, Unit] = {
    val latest    = blobNameLatestForService(service)
    val versioned = blobNameVersionedForService(service)

    encodeAndWrite(stream, List(latest, versioned))
  }

  private def readAndDecode(
      blobName: String
  ): ZIO[ZFileService[Advert] & ZStorageService, Throwable, List[Advert]] = for {
    file   <- ZStorageService.read(StorageConvention.bucketName, blobName)
    stream <- ZFileService.read[Advert](file).runCollect
    list = stream.toList
  } yield list

  protected[adverts] val readAndDecodeLatest: ZIO[ZFileService[Advert] & ZStorageService, Throwable, List[Advert]] =
    readAndDecode(blobNameLatest)

  protected[adverts] def readAndDecodeLatestForService(
      service: AdvertService
  ): ZIO[ZFileService[Advert] & ZStorageService, Throwable, List[Advert]] =
    readAndDecode(blobNameLatestForService(service))

  val live: ZLayer[ZFileService[Advert] & ZStorageService, Throwable, AdvertStore] =
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
