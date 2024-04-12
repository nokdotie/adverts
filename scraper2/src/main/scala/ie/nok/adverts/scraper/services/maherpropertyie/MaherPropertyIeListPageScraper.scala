package ie.nok.adverts.scraper.services.maherpropertyie

import org.jsoup.nodes.Document
import java.net.URL
import ie.nok.adverts.scraper.services.ServiceListPageScraper
import scala.collection.JavaConverters.iterableAsScalaIterableConverter
import scala.util.chaining.scalaUtilChainingOps
import ie.nok.adverts.scraper.services.JsoupHelper

object MaherPropertyIeListPageScraper extends ServiceListPageScraper {

  override def getNextPageUrl(document: Document): Option[URL] =
    JsoupHelper
      .findAttributeHref(document, ".pagination > a.current + a")
      .map { URL(_) }

  override def getItemPageUrls(document: Document): Iterable[URL] =
    JsoupHelper
      .filterAttributesHref(document, ".searched-properties-wrapper .property-item > h4 > a")
      .map { URL(_) }

}
