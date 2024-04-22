package ie.nok.adverts.scraper.services.maherpropertyie

import ie.nok.adverts.scraper.services.{SelectedListPageScraper, SitemapListPageScraper}
import java.net.URL

object MaherPropertyIeListPageScraper extends SitemapListPageScraper with SelectedListPageScraper {
  override def getUrls() = List(
    URL("https://maherproperty.ie/property-sitemap.xml")
  )
}
