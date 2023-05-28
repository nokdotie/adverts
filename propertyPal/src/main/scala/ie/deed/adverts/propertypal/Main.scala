package ie.nok.adverts.propertypal

import ie.nok.adverts.Record
import ie.nok.adverts.utils.gcp.GoogleCloudStorage
import ie.nok.adverts.utils.zio.File
import java.time.Instant
import scala.util.chaining._
import zio.{ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    Properties.stream
      .via(Property.pipeline)
      .debug("Property")
      .pipe { File.createTempJsonLinesFile(_) }
      .flatMap { file =>
        GoogleCloudStorage
          .upload("propertypal.com", file)
          .tap { _ => ZIO.attempt { file.delete() } }
      }
      .provide(ClientConfig.default, Client.fromConfig)
}
