package ie.nok.adverts.scraper.services.myhomeie

import ie.nok.adverts.scraper.services.SitemapListPageScraper
import java.net.URL
import org.jsoup.nodes.Document

object MyHomeIeListPageScraper extends SitemapListPageScraper {

  override def getFirstPageUrl()                  = URL("https://www.myhome.ie/sitemaps/residentialforsale_sitemap.xml")
  override def getNextPageUrl(document: Document) = None

}
