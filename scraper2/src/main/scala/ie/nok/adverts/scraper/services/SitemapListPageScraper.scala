package ie.nok.adverts.scraper.services

import ie.nok.adverts.scraper.jsoup.JsoupHelper
import java.net.URL
import org.jsoup.nodes.Document

trait SitemapListPageScraper extends ServiceListPageScraper {

  override def getItemPageUrls(document: Document): Iterable[URL] =
    JsoupHelper
      .filterStrings(document, "urlset > url > loc")
      .map { URL(_) }

}
