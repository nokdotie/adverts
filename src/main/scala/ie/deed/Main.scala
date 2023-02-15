package ie.deed

import zio._
import zio.http._
import zio.json._
import zio.stream._
import ie.deed.websites._
import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import java.io.{ByteArrayInputStream, File}
import scala.util.chaining._
import com.google.auth.oauth2._
import com.google.cloud.storage._

object Main extends ZIOAppDefault:

  def writeToFile(stream: ZStream[Client, Throwable, Record]) =
    val file = File.createTempFile("tmp", null)
    json.writeJsonLinesAs(file, stream).map { _ => file }

  val googleApplicationCredentials = sys
    .env("GOOGLE_APPLICATION_CREDENTIALS")
    .getBytes
    .pipe { ByteArrayInputStream(_) }
    .pipe { GoogleCredentials.fromStream }
  def uploadToGoogleCloudStorage(file: File) =
    val filePath = DateTimeFormatter
      .ofPattern("yyyyMMddHHmmss")
      .withZone(ZoneOffset.UTC)
      .format(Instant.now)
      .pipe { _ + ".jsonl" }

    val storage = StorageOptions
      .newBuilder()
      .setCredentials(googleApplicationCredentials)
      .build()
      .getService()

    val blobId = BlobId.of("deed-ie-scraper", filePath)
    val blobInfo = BlobInfo.newBuilder(blobId).build();

    storage.createFrom(blobInfo, file.toPath)

  def run =
    SherryFitzIe.scrape
      .concat(DngIe.scrape)
      .pipe { writeToFile }
      .map { uploadToGoogleCloudStorage }
      .debug("File")
      .provide(ClientConfig.default, Client.fromConfig)
