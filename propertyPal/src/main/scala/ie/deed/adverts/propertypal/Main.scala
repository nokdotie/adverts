package ie.nok.adverts.propertypal

import ie.nok.adverts.utils.gcp.GoogleCloudStorage
import ie.nok.adverts.utils.zio.File
import ie.nok.gcp.storage.Storage
import scala.util.chaining._
import zio.{Scope, ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    BuildId.latest
      .flatMap { buildId =>
        Properties
          .stream(buildId)
          .via(Property.pipeline(buildId))
          .debug("Property")
          .pipe { File.createTempJsonLinesFile(_) }
      }
      .flatMap { file =>
        GoogleCloudStorage
          .upload("propertypal.com", file)
          .tap { _ => ZIO.attempt { file.delete() } }
      }
      .provide(
        ClientConfig.default,
        Client.fromConfig,
        Storage.live,
        Scope.default
      )
}
