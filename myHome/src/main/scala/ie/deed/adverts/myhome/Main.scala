package ie.nok.adverts.myhome

import ie.nok.adverts.Advert
import ie.nok.adverts.utils.gcp.GoogleCloudStorage
import ie.nok.adverts.utils.zio.File
import java.time.Instant
import scala.util.chaining._
import zio.{ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    Properties.stream
      .debug("Property")
      .pipe { File.createTempJsonLinesFile(_) }
      .flatMap { file =>
        GoogleCloudStorage
          .upload("myhome.ie", file)
          .tap { _ => ZIO.attempt { file.delete() } }
      }
      .provide(ClientConfig.default, Client.fromConfig)
}
