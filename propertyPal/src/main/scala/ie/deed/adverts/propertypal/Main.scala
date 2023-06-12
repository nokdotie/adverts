package ie.nok.adverts.propertypal

import ie.nok.adverts.utils.gcp.GoogleCloudStorage
import ie.nok.adverts.utils.zio.File
import scala.util.chaining._
import zio.{ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    BuildId.latest
      .flatMap { buildId =>
        Properties.stream(buildId)
          .via(Property.pipeline(buildId))
          .debug("Property")
          .pipe { File.createTempJsonLinesFile(_) }
      }
      .flatMap { file =>
        GoogleCloudStorage
          .upload("propertypal.com", file)
          .tap { _ => ZIO.attempt { file.delete() } }
      }
      .provide(ClientConfig.default, Client.fromConfig)
}
