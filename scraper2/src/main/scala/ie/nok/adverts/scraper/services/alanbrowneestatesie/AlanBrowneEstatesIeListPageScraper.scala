package ie.nok.adverts.scraper.services.alanbrowneestatesie

import ie.nok.adverts.scraper.jsoup.JsoupHelper
import ie.nok.adverts.scraper.services.ServiceListPageScraper
import org.jsoup.nodes.Document
import java.net.{URL, URLEncoder}

object AlanBrowneEstatesIeListPageScraper extends ServiceListPageScraper {

  override def getFirstPageUrl() = URL("https://alanbrowneestates.ie/properties.php")

  def getNextPageUrl(document: Document): Option[URL] = None

  def getItemPageUrls(document: Document): Iterable[URL] =
    JsoupHelper
      .filterAttributesHref(document, "#properties a")
      .map { _.replaceAll(" ", "%20") }
      .map { URL(_) }

}
