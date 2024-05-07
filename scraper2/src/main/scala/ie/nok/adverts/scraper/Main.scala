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
import zio.http.model.Headers
import zio.stream.{ZPipeline, ZStream}
import zio.Schedule.{fixed, recurs}
import scala.util.Random

object Main extends ZIOAppDefault {

  // Top user agents from https://www.useragents.me/
  val userAgentHeader = List(
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.3",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.3",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.1",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.0.",
    "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Mobile Safari/537.3",
    "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4.1 Mobile/15E148 Safari/604.",
    "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/24.0 Chrome/117.0.0.0 Mobile Safari/537.3"
  ).pipe { Random.shuffle }.head.pipe { Headers.userAgent }

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
      .requestBodyAsHtml(
        url.toString(),
        headers = userAgentHeader
      )
      .delay(250.millis)
      // MyHome.ie fails unexpectedly with 500 errors
      .filterOrFail { _.title != "Error" } { new Throwable(s"Error getting document: $url") }
      .retry(recurs(3) && fixed(3.second))

  def getItemPageUrls(listPageScraper: ServiceListPageScraper): ZStream[ZioClient, Throwable, URL] =
    ZStream
      .paginateZIO(listPageScraper.getFirstPageUrl()) { url =>
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
        getItemPageUrls(serviceScraper.getListPageScraper())
          .via(getAdvert(serviceScraper.getItemPageScraper()))
          .pipe { encodeAndWriteForService(serviceScraper.getService(), _) }
          .provide(ZioClient.default, ZFileAndGoogleStorageStoreImpl.layer[Advert])
      }
}
