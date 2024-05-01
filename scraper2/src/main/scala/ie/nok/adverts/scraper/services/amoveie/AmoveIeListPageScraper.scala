package ie.nok.adverts.scraper.services.amoveie

import ie.nok.adverts.scraper.services.{SelectedListPageScraper, SitemapListPageScraper}
import java.net.URL

object AmoveIeListPageScraper extends SitemapListPageScraper with SelectedListPageScraper {
  override def getUrls() = List(
    URL("https://amove.ie/listing-sitemap.xml")
  )

}
