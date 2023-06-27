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

private def getTimestampBlobName(service: String, instant: Instant): String =
  DateTimeFormatter
    .ofPattern("yyyyMMddHHmmss")
    .withZone(ZoneOffset.UTC)
    .format(instant)
    .pipe { getBlobName(service, _) }

private def getLatestBlobName(service: String): String =
  getBlobName(service: String, "latest")

private def writeToTempFile[R](
    stream: ZStream[R, Throwable, Advert]
): ZIO[R & Scope, Throwable, Path] = for {
  path <- Files.createTempFileScoped()
  _ <- writeJsonLinesAs(path.toFile, stream)
} yield path

private def uploadToGcpStorage(
    path: Path,
    blobName: String
): ZIO[Storage, Throwable, Unit] = for {
  bucket <- AdvertStoreImpl.bucket
  blobInfo = BlobId
    .of(bucket, blobName)
    .pipe(BlobInfo.newBuilder)
    .build()

  _ <- createFrom(blobInfo, path.toFile.toPath, List.empty)
} yield ()

def writeToGcpStorate[R](
    service: String,
    stream: ZStream[R, Throwable, Advert]
): ZIO[R & Scope & Storage, Throwable, Unit] = for {
  path <- writeToTempFile(stream)

  _ <- uploadToGcpStorage(path, getLatestBlobName(service))
  _ <- uploadToGcpStorage(path, getTimestampBlobName(service, Instant.now))
} yield ()
