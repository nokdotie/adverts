package ie.nok.adverts.utils.gcp

import ie.nok.env.Environment
import ie.nok.gcp.storage
import ie.nok.gcp.storage.Storage
import com.google.cloud.storage.{BlobId, BlobInfo}
import ie.nok.gcp.auth.GoogleCredentials
import java.io.{ByteArrayInputStream, File}
import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO

object GoogleCloudStorage {
  private def getFilePath(prefix: String): String =
    DateTimeFormatter
      .ofPattern("yyyyMMddHHmmss")
      .withZone(ZoneOffset.UTC)
      .format(Instant.now)
      .pipe { instant => s"adverts/$prefix/$instant.jsonl" }

  private val bucket: ZIO[Any, Throwable, String] =
    Environment.get
      .map {
        case Environment.Production => "nok-ie"
        case Environment.Other      => "nok-ie-dev"
      }

  def upload(prefix: String, file: File): ZIO[Storage, Throwable, Unit] =
    for {
      bucket <- bucket
      filePath = getFilePath(prefix)
      blobInfo = BlobId
        .of(bucket, filePath)
        .pipe(BlobInfo.newBuilder)
        .build()
      _ <- storage.createFrom(blobInfo, file.toPath(), List.empty)
    } yield ()
}
