package ie.nok.adverts.scraper.services.daftie

import ie.nok.adverts.scraper.jsoup.JsoupHelper
import ie.nok.adverts.scraper.services.ServiceListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object DaftIeListPageScraper extends ServiceListPageScraper {

  override def getFirstPageUrl() = URL("https://www.daft.ie/property-for-sale/ireland")

  override def getNextPageUrl(document: Document): Option[URL] = {
    val nextPage = JsoupHelper
      .findAttributeHref(document, "a[rel=next]")
      .map { URL(_) }

    nextPage.filter(!_.getQuery().contains("from=0"))
  }

  override def getItemPageUrls(document: Document): Iterable[URL] =
    JsoupHelper
      .filterAttributesHref(document, "ul[data-testid=results]>li[data-testid^=result-]>a")
      .map { URL(_) }
}
