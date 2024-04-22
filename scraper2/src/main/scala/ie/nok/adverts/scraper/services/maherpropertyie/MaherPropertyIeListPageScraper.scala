package ie.nok.adverts.scraper.services.maherpropertyie

import ie.nok.adverts.scraper.jsoup.JsoupHelper
import ie.nok.adverts.scraper.services.ServiceListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object MaherPropertyIeListPageScraper extends ServiceListPageScraper {

  override def getNextPageUrl(document: Document): Option[URL] = None

  override def getItemPageUrls(document: Document): Iterable[URL] =
    JsoupHelper
      .filterAttributesHref(document, "#sitemap a")
      .drop(1)
      .map { URL(_) }
}
