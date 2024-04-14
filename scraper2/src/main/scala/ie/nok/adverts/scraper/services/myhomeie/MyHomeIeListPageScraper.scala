package ie.nok.adverts.scraper.services.myhomeie

import ie.nok.adverts.scraper.services.{SelectedListPageScraper, SitemapListPageScraper}
import java.net.URL

object MyHomeIeListPageScraper extends SitemapListPageScraper with SelectedListPageScraper {
  override def getUrls() = List(
    URL("https://www.myhome.ie/sitemaps/residentialforsale_sitemap.xml")
  )
}
