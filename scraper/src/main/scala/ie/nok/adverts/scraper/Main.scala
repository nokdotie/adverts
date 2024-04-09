package ie.nok.adverts.scraper

import ie.nok.advertisers.stores.{AdvertiserStore, AdvertiserStoreInMemory}
import ie.nok.adverts.stores.AdvertStoreImpl.encodeAndWriteForService
import ie.nok.adverts.{Advert, AdvertService}
import zio.http.Client as ZioClient
import zio.stream.{ZPipeline, ZStream}
import zio.{Scope, Console, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer, durationInt}
import ie.nok.stores.compose.{ZFileAndGoogleStorageStore, ZFileAndGoogleStorageStoreImpl}
import java.net.URL
import org.jsoup.nodes.Document
import ie.nok.http.Client
import zio.Schedule.{fixed, recurs}
import ie.nok.adverts.scraper.services.daftie.DaftIeScraper
import ie.nok.adverts.scraper.services.{ServiceListPageScraper, ServiceItemPageScraper}

// object Main extends ZIOAppDefault {

//   val getAdvertService: ZIO[ZIOAppArgs, Throwable, AdvertService] =
//     getArgs
//       .map { _.headOption }
//       .someOrFail(new Throwable("No advert service provided"))
//       .flatMap { arg => ZIO.attempt { AdvertService.valueOf(arg) } }

//   def getAdvertStream(
//       advertService: AdvertService
//   ): ZStream[Client & AdvertiserStore, Throwable, Advert] =
//     advertService match {
//       case AdvertService.DaftIe          => daftie.advertStream
//       case AdvertService.DngIe           => dngie.advertStream
//       case AdvertService.MyHomeIe        => myhomeie.advertStream
//       case AdvertService.PropertyPalCom  => propertypalcom.advertStream
//       case AdvertService.SherryFitzIe    => sherryfitzie.advertStream
//       case AdvertService.MaherPropertyIe => maherpropertyie.advertStream
//     }

//   def run: ZIO[ZIOAppArgs with Scope, Throwable, Unit] = for {
//     advertService <- getAdvertService
//     advertStream = getAdvertStream(advertService)
//     _ <- encodeAndWriteForService(advertService, advertStream)
//       .provide(
//         Client.default,
//         ZFileAndGoogleStorageStoreImpl.layer[Advert],
//         AdvertiserStoreInMemory.live
//       )
//   } yield ()

// }

def getDocument(url: URL): ZIO[ZioClient, Throwable, Document] =
  Client
    .requestBodyAsHtml(url.toString())
    .retry(recurs(3) && fixed(1.second))
    .map { document =>
      document.setBaseUri(url.toString())
      document
    }

def getItemPageUrls(initialListPageUrl: URL, listPageScraper: ServiceListPageScraper): ZStream[ZioClient, Throwable, URL] =
  ZStream
    .paginateZIO(initialListPageUrl) { url =>
      getDocument(url)
        .map { document =>
          val nextPageUrl  = listPageScraper.getNextPageUrl(document)
          val itemPageUrls = listPageScraper.getItemPageUrls(document)

          (itemPageUrls, nextPageUrl)
        }
    }
    .flattenIterables

def getAdvert(
    itemPageScraper: ServiceItemPageScraper
): ZPipeline[ZioClient, Throwable, URL, Advert] =
  ZPipeline
    .mapZIOPar(3)(getDocument)
    .map(itemPageScraper.getAdvert)

object Main extends ZIOAppDefault {
  val serviceScraper = DaftIeScraper

  def run =
    getItemPageUrls(serviceScraper.getInitialListPageUrl, serviceScraper.getListPageScraper)
      .via(getAdvert(serviceScraper.getItemPageScraper))
      .map { _.advertUrl }
      .zipWithIndex
      .runForeach { Console.printLine(_) }
      .provide(ZioClient.default)
}
