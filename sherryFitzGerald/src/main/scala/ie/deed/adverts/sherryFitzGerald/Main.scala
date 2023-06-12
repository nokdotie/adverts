package ie.nok.adverts.sherryFitzGerald

import ie.nok.adverts.utils.gcp.GoogleCloudStorage
import ie.nok.adverts.utils.zio.File
import scala.util.chaining._
import zio.{ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    Properties.stream
      .via(Property.pipeline)
      .debug("Property")
      .pipe { File.createTempJsonLinesFile }
      .flatMap { file =>
        GoogleCloudStorage
          .upload("sherryfitz.ie", file)
          .tap { _ => ZIO.attempt { file.delete() } }
      }
      .provide(ClientConfig.default, Client.fromConfig)
}
