package ie.nok.adverts.dooglasNewmanGood

import ie.nok.adverts.AdvertService.DooglasNewmanGoodIe
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.gcp.storage.Storage
import scala.util.chaining._
import zio.{Scope, ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    Properties.stream
      .debug("Property")
      .pipe { AdvertStoreImpl.encodeAndWriteForService(DooglasNewmanGoodIe, _) }
      .provide(
        ClientConfig.default,
        Client.fromConfig,
        Storage.live,
        Scope.default
      )
}
