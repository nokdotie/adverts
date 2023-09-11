package ie.nok.adverts.stores

import com.google.cloud.storage.BlobInfo
import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.json.JsonDecoder
import ie.nok.env.Environment
import ie.nok.gcp.storage.{createFrom, readAllBytes, Storage}
import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, Scope, ZIO, ZLayer}
import zio.json.writeJsonLinesAs
import zio.nio.file.Files
import zio.stream.ZStream

object AdvertStoreImpl {
  private val bucket: ZIO[Any, Throwable, String] =
    Environment.get
      .map {
        case Environment.Production => "nok-ie"
        case Environment.Other      => "nok-ie-dev"
      }

  private val blobNameLatest: String = "adverts/latest.jsonl"
  private def blobNameLatestForService(service: AdvertService): String =
    s"adverts/${service.host}/latest.jsonl"
  private def blobNameVersionedForService(service: AdvertService): String =
    DateTimeFormatter
      .ofPattern("yyyyMMddHHmmss")
      .withZone(ZoneOffset.UTC)
      .format(Instant.now)
      .pipe { timestamp => s"adverts/${service.host}/$timestamp.jsonl" }

  private def encodeAndWrite[R](
      stream: ZStream[R, Throwable, Advert],
      blobNames: List[String]
  ): ZIO[R & Scope & Storage, Throwable, Unit] = for {
    path <- Files.createTempFileScoped()
    _ <- writeJsonLinesAs(path.toFile, stream)

    bucket <- bucket
    _ <- blobNames
      .map { BlobInfo.newBuilder(bucket, _).build }
      .map { createFrom(_, path.toFile.toPath, List.empty) }
      .pipe { ZIO.collectAll }
  } yield ()

  protected[adverts] def encodeAndWriteLatest[R](
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & Scope & Storage, Throwable, Unit] =
    encodeAndWrite(stream, List(blobNameLatest))

  protected[adverts] def encodeAndWriteForService[R](
      service: AdvertService,
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & Scope & Storage, Throwable, Unit] = {
    val latest = blobNameLatestForService(service)
    val versioned = blobNameVersionedForService(service)

    encodeAndWrite(stream, List(latest, versioned))
  }

  private def readAndDecode(
      blobName: String
  ): ZIO[Storage, Throwable, List[Advert]] = for {
    bucket <- bucket
    allBytes <- readAllBytes(bucket, blobName, List.empty)
    all <- allBytes
      .pipe { new String(_) }
      .linesIterator
      .map { JsonDecoder.decode[Advert] }
      .toList
      .pipe { ZIO.collectAll }
  } yield all

  protected[adverts] val readAndDecodeLatest
      : ZIO[Storage, Throwable, List[Advert]] =
    readAndDecode(blobNameLatest)

  protected[adverts] def readAndDecodeLatestForService(
      service: AdvertService
  ): ZIO[Storage, Throwable, List[Advert]] =
    readAndDecode(blobNameLatestForService(service))

  val live: ZLayer[Storage, Throwable, AdvertStore] =
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
      .drop(after.index)
      .take(first)
      .pipe { ZIO.succeed }

}
