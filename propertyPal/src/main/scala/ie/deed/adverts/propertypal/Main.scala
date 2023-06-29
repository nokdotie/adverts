package ie.nok.adverts.propertypal

import ie.nok.adverts.AdvertService.PropertyPalCom
import ie.nok.adverts.stores.AdvertStoreImpl
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
          .pipe {
            AdvertStoreImpl.encodeAndWriteForService(PropertyPalCom, _)
          }
      }
      .provide(
        ClientConfig.default,
        Client.fromConfig,
        Storage.live,
        Scope.default
      )
}
