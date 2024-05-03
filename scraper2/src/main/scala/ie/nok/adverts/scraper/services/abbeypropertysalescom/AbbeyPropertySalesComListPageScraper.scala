package ie.nok.adverts.scraper.services.abbeypropertysalescom

import ie.nok.adverts.scraper.services.{SelectedListPageScraper, SitemapListPageScraper}
import java.net.URL

object AbbeyPropertySalesComListPageScraper extends SitemapListPageScraper with SelectedListPageScraper {
  override def getUrls() = List(
    URL("https://www.abbeypropertysales.com/wp-sitemap-posts-property-1.xml")
  )

}
