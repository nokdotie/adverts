package ie.nok.adverts.scraper

import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.advertisers.stores.{AdvertiserStore, AdvertiserStoreInMemory}
import ie.nok.adverts.stores.AdvertStoreImpl.encodeAndWriteForService
import ie.nok.gcp.storage.Storage
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Random
import zio.{Scope, Console, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.Client
import zio.stream.ZStream

object Main extends ZIOAppDefault {

  val getAdvertService: ZIO[ZIOAppArgs, Throwable, AdvertService] =
    getArgs
      .map { _.headOption }
      .someOrFail(new Throwable("No advert service provided"))
      .flatMap { arg => ZIO.attempt { AdvertService.valueOf(arg) } }

  def getAdvertStream(
      advertService: AdvertService
  ): ZStream[Client & AdvertiserStore, Throwable, Advert] =
    advertService match {
      case AdvertService.DaftIe         => daftie.advertStream
      case AdvertService.DngIe          => dngie.advertStream
      case AdvertService.MyHomeIe       => myhomeie.advertStream
      case AdvertService.PropertyPalCom => propertypalcom.advertStream
      case AdvertService.SherryFitzIe   => sherryfitzie.advertStream
    }

  def run = for {
    advertService <- getAdvertService
    advertStream = getAdvertStream(advertService)
    _ <- encodeAndWriteForService(advertService, advertStream)
      .provide(
        Client.default,
        Scope.default,
        Storage.live,
        AdvertiserStoreInMemory.live
      )
  } yield ()

}
