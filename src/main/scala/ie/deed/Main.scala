package ie.deed

import zio.*
import zio.http.*
import zio.json.*
import zio.stream.*
import ie.deed.websites.*

import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import java.io.{ByteArrayInputStream, File}
import scala.util.chaining.*
import com.google.auth.oauth2.*
import com.google.cloud.storage.{Blob, BlobId, BlobInfo, StorageOptions}

object Main extends ZIOAppDefault:

  def writeToFile(
      stream: ZStream[Client, Throwable, Record]
  ): ZIO[Client, Throwable, File] = {
    val file = File.createTempFile("deed_", ".json")
    json.writeJsonLinesAs(file, stream).map { _ => file }
  }

  lazy val googleApplicationCredentials = sys
    .env("GOOGLE_APPLICATION_CREDENTIALS")
    .getBytes
    .pipe { ByteArrayInputStream(_) }
    .pipe { GoogleCredentials.fromStream }

  def uploadToGoogleCloudStorage(fileOpt: Option[File]): Option[Blob] = {
    fileOpt.map { file =>
      val filePath = DateTimeFormatter
        .ofPattern("yyyyMMddHHmmss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now)
        .pipe { _ + ".jsonl" }

      val storage = StorageOptions
        .newBuilder()
        .setCredentials(googleApplicationCredentials)
        .build()
        .getService

      val blobId = BlobId.of("deed-ie-scraper", filePath)
      val blobInfo = BlobInfo.newBuilder(blobId).build();

      storage.createFrom(blobInfo, file.toPath)
    }
  }

  def run: ZIO[Any, Any, Option[Blob]] =
    ZIO
      .service[ScraperConfig]
      .flatMap { scraperConfig =>
        SherryFitzIe(scraperConfig).scrape
          .concat(DngIe(scraperConfig).scrape)
          .debug("Record")
          .pipe { writeToFile }
          .debug("File")
          .when(scraperConfig.uploadToStorage)
          .map { uploadToGoogleCloudStorage }
          .debug("Blob")
          .provide(ClientConfig.default, Client.fromConfig)
      }
      .provide(ScraperConfig.layer)
