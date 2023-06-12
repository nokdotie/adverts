package ie.nok.adverts

import com.google.cloud.storage.{BlobId, BlobInfo}
import ie.nok.env.Environment
import ie.nok.gcp.storage.{createFrom, Storage}
import java.io.File
import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.json.{writeJsonLinesAs, JsonEncoder}
import zio.stream.ZStream

private val bucket: ZIO[Any, Throwable, String] =
  Environment.get
    .map {
      case Environment.Production => "nok-ie"
      case Environment.Other      => "nok-ie-dev"
    }

private def getBlobName(prefix: String): String =
  DateTimeFormatter
    .ofPattern("yyyyMMddHHmmss")
    .withZone(ZoneOffset.UTC)
    .format(Instant.now)
    .pipe { instant => s"adverts/$prefix/$instant.jsonl" }

def writeToGcpStorate[R](
    prefix: String,
    stream: ZStream[R, Throwable, Advert]
): ZIO[R & Storage, Throwable, Unit] = for {
  file <- ZIO.attempt { File.createTempFile("tmp", null) }
  _ <- writeJsonLinesAs(file, stream)
  bucket <- bucket
  blobName = getBlobName(prefix)
  blobInfo = BlobId
    .of(bucket, blobName)
    .pipe(BlobInfo.newBuilder)
    .build()
  _ <- createFrom(blobInfo, file.toPath(), List.empty)
  _ <- ZIO.attempt { file.delete() }
} yield ()
