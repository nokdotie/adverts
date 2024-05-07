package ie.nok.adverts.scraper.services.maherpropertyie

import ie.nok.adverts.scraper.services.SitemapListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object MaherPropertyIeListPageScraper extends SitemapListPageScraper {

  override def getFirstPageUrl()                  = URL("https://maherproperty.ie/property-sitemap.xml")
  override def getNextPageUrl(document: Document) = None

}
