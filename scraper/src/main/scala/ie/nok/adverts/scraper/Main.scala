package ie.nok.adverts.scraper

import ie.nok.advertisers.stores.{AdvertiserStore, AdvertiserStoreInMemory}
import ie.nok.adverts.stores.AdvertStoreImpl.encodeAndWriteForService
import ie.nok.adverts.{Advert, AdvertService}
import zio.http.Client
import zio.stream.ZStream
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import ie.nok.stores.compose.{ZFileAndGoogleStorageStore, ZFileAndGoogleStorageStoreImpl}

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
      case AdvertService.DngIe        => dngie.advertStream
      case AdvertService.SherryFitzIe => sherryfitzie.advertStream
      case _                          => throw new Throwable("Advert service not supported")
    }

  def run: ZIO[ZIOAppArgs with Scope, Throwable, Unit] = for {
    advertService <- getAdvertService
    advertStream = getAdvertStream(advertService)
    _ <- encodeAndWriteForService(advertService, advertStream)
      .provide(
        Client.default,
        ZFileAndGoogleStorageStoreImpl.layer[Advert],
        AdvertiserStoreInMemory.live
      )
  } yield ()

}
