package ie.nok.adverts.scraper.services.abbeypropertysalescom

import ie.nok.adverts.scraper.services.SitemapListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object AbbeyPropertySalesComListPageScraper extends SitemapListPageScraper {

  override def getFirstPageUrl()                  = URL("https://www.abbeypropertysales.com/wp-sitemap-posts-property-1.xml")
  override def getNextPageUrl(document: Document) = None

}
