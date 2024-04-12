package ie.nok.adverts.scraper.services.myhomeie

import ie.nok.adverts.scraper.services.{JsoupHelper, ServiceListPageScraper}
import java.net.URL
import org.jsoup.nodes.Document

object MyHomeIeListPageScraper extends ServiceListPageScraper {

  override def getNextPageUrl(document: Document): Option[URL] =
    None

  override def getItemPageUrls(document: Document): Iterable[URL] =
    JsoupHelper
      .filterStrings(document, "urlset > url > loc")
      .map { URL(_) }
}
