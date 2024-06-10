package ie.nok.adverts.scraper.services.propertypartnersie

import ie.nok.adverts.scraper.jsoup.JsoupHelper
import ie.nok.adverts.scraper.services.ServiceListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object PropertyPartnersIeListPageScraper extends ServiceListPageScraper {

  override def getFirstPageUrl() = URL("https://www.propertypartners.ie/residential/results?status=2%7C3")
  override def getNextPageUrl(document: Document) =
    JsoupHelper
      .findAttributeHref(document, "a[rel=next]")
      .map { _.replace("|", "%7C") }
      .map { URL(_) }

  override def getItemPageUrls(document: Document) =
    JsoupHelper
      .filterAttributesHref(document, "a.propertyLink")
      .map { URL(_) }
}
