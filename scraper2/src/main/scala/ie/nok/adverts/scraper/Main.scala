package ie.nok.adverts.scraper

import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.adverts.stores.AdvertStoreImpl.encodeAndWriteForService
import ie.nok.adverts.scraper.services.{ServiceScraper, ServiceListPageScraper, ServiceItemPageScraper}
import ie.nok.http.Client
import ie.nok.stores.compose.ZFileAndGoogleStorageStoreImpl
import java.net.URL
import org.jsoup.nodes.Document
import scala.util.chaining.scalaUtilChainingOps
import zio.{Console, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, durationInt}
import zio.http.Client as ZioClient
import zio.stream.{ZPipeline, ZStream}
import zio.Schedule.{fixed, recurs}

object Main extends ZIOAppDefault {

  val getService: ZIO[ZIOAppArgs, Throwable, ServiceScraper] =
    getArgs
      .map { _.headOption }
      .someOrFail(new Throwable("No advert service provided"))
      .flatMap { arg => ZIO.attempt { AdvertService.valueOf(arg) } }
      .map { service =>
        ServiceScraper.all
          .find { _.getService() == service }
          .getOrElse { throw new Throwable(s"Advert service not found: $service") }
      }

  def getDocument(url: URL): ZIO[ZioClient, Throwable, Document] =
    Client
      .requestBodyAsHtml(url.toString())
      .delay(250.millis)
      // MyHome.ie fails unexpectedly with 500 errors
      .filterOrFail { _.title != "Error" } { new Throwable(s"Error getting document: $url") }
      .retry(recurs(3) && fixed(3.second))

  def getItemPageUrls(initialListPageUrl: URL, listPageScraper: ServiceListPageScraper): ZStream[ZioClient, Throwable, URL] =
    ZStream
      .paginateZIO(initialListPageUrl) { url =>
        getDocument(url)
          .tap { _ => Console.printLine(s"Got list: $url") }
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
      .mapZIOPar(5) { case url: URL =>
        getDocument(url)
          .tap { _ => Console.printLine(s"Got item: $url") }
      }
      .filter { itemPageScraper.filter }
      .map { itemPageScraper.getAdvert }

  def run: ZIO[ZIOAppArgs with Scope, Throwable, Unit] =
    getService
      .flatMap { serviceScraper =>
        getItemPageUrls(serviceScraper.getInitialListPageUrl(), serviceScraper.getListPageScraper())
          .via(getAdvert(serviceScraper.getItemPageScraper()))
          .pipe { encodeAndWriteForService(serviceScraper.getService(), _) }
          .provide(ZioClient.default, ZFileAndGoogleStorageStoreImpl.layer[Advert])
      }
}
