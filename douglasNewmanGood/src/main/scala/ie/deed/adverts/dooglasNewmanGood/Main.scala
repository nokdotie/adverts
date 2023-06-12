package ie.nok.adverts.dooglasNewmanGood

import ie.nok.adverts.writeToGcpStorate
import ie.nok.gcp.storage.Storage
import scala.util.chaining._
import zio.{Scope, ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    Properties.stream
      .debug("Property")
      .pipe { writeToGcpStorate("dng.ie", _) }
      .provide(
        ClientConfig.default,
        Client.fromConfig,
        Storage.live,
        Scope.default
      )
}
