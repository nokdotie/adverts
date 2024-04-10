package ie.nok.adverts.scraper

import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.adverts.stores.AdvertStoreImpl.encodeAndWriteForService
import ie.nok.adverts.scraper.services.{ServiceScraper, ServiceListPageScraper, ServiceItemPageScraper}
import ie.nok.http.Client
import ie.nok.stores.compose.ZFileAndGoogleStorageStoreImpl
import java.net.URL
import org.jsoup.nodes.Document
import scala.util.chaining.scalaUtilChainingOps
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, durationInt}
import zio.http.Client as ZioClient
import zio.stream.{ZPipeline, ZStream}
import zio.Schedule.{fixed, recurs}

object Main extends ZIOAppDefault {

  val getService: ZIO[ZIOAppArgs, Throwable, ServiceScraper] =
    getArgs
      .map { _.headOption }
      .someOrFail(new Throwable("No advert service provided"))
      .flatMap { arg => ZIO.attempt { AdvertService.valueOf(arg) } }
      .map { service => ServiceScraper.all.find { _.getService() == service }.get }

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
      .mapZIOPar(5)(getDocument)
      .map(itemPageScraper.getAdvert)

  def run: ZIO[ZIOAppArgs with Scope, Throwable, Unit] =
    getService
      .flatMap { serviceScraper =>
        getItemPageUrls(serviceScraper.getInitialListPageUrl(), serviceScraper.getListPageScraper())
          .via(getAdvert(serviceScraper.getItemPageScraper()))
          .pipe { encodeAndWriteForService(serviceScraper.getService(), _) }
          .provide(ZioClient.default, ZFileAndGoogleStorageStoreImpl.layer[Advert])
      }
}
