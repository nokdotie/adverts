package ie.nok.adverts

import com.google.cloud.storage.{BlobId, BlobInfo}
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.gcp.storage.{createFrom, Storage}
import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps
import zio.{Scope, ZIO}
import zio.json.writeJsonLinesAs
import zio.nio.file.{Files, Path}
import zio.stream.ZStream

private def getBlobName(
    service: String,
    fileNameWithoutExtension: String
): String =
  s"adverts/$service/$fileNameWithoutExtension.jsonl"

private def getTimestampBlobName(service: String): String =
  DateTimeFormatter
    .ofPattern("yyyyMMddHHmmss")
    .withZone(ZoneOffset.UTC)
    .format(Instant.now)
    .pipe { getBlobName(service, _) }

private def getLatestBlobName(service: String): String =
  getBlobName(service: String, "latest")

private def writeToTempFileScoped[R](
    stream: ZStream[R, Throwable, Advert]
): ZIO[R & Scope, Throwable, Path] = for {
  path <- Files.createTempFileScoped()
  _ <- writeJsonLinesAs(path.toFile, stream)
} yield path

def writeToGcpStorate[R](
    service: String,
    stream: ZStream[R, Throwable, Advert]
): ZIO[R & Scope & Storage, Throwable, Unit] = for {
  path <- writeToTempFileScoped(stream)

  bucket <- AdvertStoreImpl.bucket

  _ <- BlobId
    .of(bucket, getLatestBlobName(service))
    .pipe(BlobInfo.newBuilder)
    .build()
    .pipe { createFrom(_, path.toFile.toPath, List.empty) }

  _ <- BlobId
    .of(bucket, getTimestampBlobName(service))
    .pipe(BlobInfo.newBuilder)
    .build()
    .pipe { createFrom(_, path.toFile.toPath, List.empty) }
} yield ()
