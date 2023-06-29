package ie.nok.adverts.sherryFitzGerald

import ie.nok.adverts.AdvertService.SherryFitzgeraldIe
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.gcp.storage.Storage
import scala.util.chaining._
import zio.{Scope, ZIO, ZIOAppDefault}
import zio.http.{Client, ClientConfig}

object Main extends ZIOAppDefault {
  def run =
    Properties.stream
      .via(Property.pipeline)
      .debug("Property")
      .pipe { AdvertStoreImpl.encodeAndWriteForService(SherryFitzgeraldIe, _) }
      .provide(
        ClientConfig.default,
        Client.fromConfig,
        Storage.live,
        Scope.default
      )
}
