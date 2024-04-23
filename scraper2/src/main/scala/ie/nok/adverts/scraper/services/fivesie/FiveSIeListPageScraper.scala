package ie.nok.adverts.scraper.services.fivesie

import ie.nok.adverts.scraper.services.{SelectedListPageScraper, SitemapListPageScraper}
import java.net.URL

object FiveSIeListPageScraper extends SitemapListPageScraper with SelectedListPageScraper {
  override def getUrls() = List(
    URL("https://www.5s.ie/property-sitemap.xml")
  )
}
