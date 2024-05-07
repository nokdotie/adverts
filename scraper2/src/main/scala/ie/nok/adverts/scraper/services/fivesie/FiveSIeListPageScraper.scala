package ie.nok.adverts.scraper.services.fivesie

import ie.nok.adverts.scraper.services.SitemapListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object FiveSIeListPageScraper extends SitemapListPageScraper {

  override def getFirstPageUrl()                  = URL("https://www.5s.ie/property-sitemap.xml")
  override def getNextPageUrl(document: Document) = None

}
