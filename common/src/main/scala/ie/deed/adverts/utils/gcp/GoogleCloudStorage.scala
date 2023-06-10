package ie.nok.adverts.utils.gcp

import com.google.cloud.storage.{BlobId, BlobInfo, StorageOptions}
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

  private def getStorageOptions(): ZIO[Any, Throwable, StorageOptions] =
    GoogleCredentials.applicationDefault
      .map { credentials =>
        StorageOptions
          .newBuilder()
          .setCredentials(credentials)
          .build()
      }

  private def getBucket() =
    sys.env
      .get("ENV")
      .pipe {
        case Some("production") => "nok-ie"
        case _                  => "nok-ie-dev"
      }

  def upload(prefix: String, file: File): ZIO[Any, Throwable, Unit] =
    for {
      storage <- getStorageOptions().map { _.getService() }
      filePath = getFilePath(prefix)
      blobInfo = BlobId
        .of(getBucket(), filePath)
        .pipe(BlobInfo.newBuilder)
        .build()
      _ <- ZIO.attempt { storage.createFrom(blobInfo, file.toPath) }
    } yield ()

}
