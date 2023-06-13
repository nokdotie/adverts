package ie.nok.adverts

import com.google.cloud.storage.{BlobId, BlobInfo}
import ie.nok.adverts.stores.AdvertsStoreImpl
import ie.nok.gcp.storage.{createFrom, Storage}
import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps
import zio.{Scope, ZIO}
import zio.json.writeJsonLinesAs
import zio.nio.file.Files
import zio.stream.ZStream

private def getBlobName(prefix: String): String =
  DateTimeFormatter
    .ofPattern("yyyyMMddHHmmss")
    .withZone(ZoneOffset.UTC)
    .format(Instant.now)
    .pipe { instant => s"adverts/$prefix/$instant.jsonl" }

def writeToGcpStorate[R](
    prefix: String,
    stream: ZStream[R, Throwable, Advert]
): ZIO[R & Scope & Storage, Throwable, Unit] = for {
  path <- Files.createTempFileScoped()
  _ <- writeJsonLinesAs(path.toFile, stream)
  bucket <- AdvertsStoreImpl.bucket
  blobName = getBlobName(prefix)
  blobInfo = BlobId
    .of(bucket, blobName)
    .pipe(BlobInfo.newBuilder)
    .build()
  _ <- createFrom(blobInfo, path.toFile.toPath, List.empty)
} yield ()
