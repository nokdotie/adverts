package ie.nok.adverts.scraper.services.amoveie

import ie.nok.adverts.scraper.services.SitemapListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object AmoveIeListPageScraper extends SitemapListPageScraper {

  override def getFirstPageUrl()                  = URL("https://amove.ie/listing-sitemap.xml")
  override def getNextPageUrl(document: Document) = None

}
