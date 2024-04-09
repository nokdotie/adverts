package ie.nok.adverts.scraper.services.daftie

import org.jsoup.nodes.Document
import java.net.URL
import ie.nok.adverts.scraper.services.ServiceListPageScraper
import scala.collection.JavaConverters.iterableAsScalaIterableConverter
import scala.util.chaining.scalaUtilChainingOps

object DaftIeListPageScraper extends ServiceListPageScraper {

  override def getNextPageUrl(document: Document): Option[URL] = {
    val nextPage = document
      .selectFirst("a[rel=next]")
      .absUrl("href")
      .pipe { new URL(_) }

    val nextPageZero = nextPage.getQuery().contains("from=0")

    Option.when(!nextPageZero)(nextPage)
  }

  override def getItemPageUrls(document: Document): Iterable[URL] =
    document
      .select("ul[data-testid=results]>li[data-testid^=result-]>a")
      .asScala
      .map { _.absUrl("href") }
      .map { new URL(_) }
}
