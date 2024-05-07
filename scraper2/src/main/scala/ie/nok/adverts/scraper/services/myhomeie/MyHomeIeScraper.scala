package ie.nok.adverts.scraper.services.myhomeie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.{ServiceScraper, SitemapListPageScraper}

object MyHomeIeScraper extends ServiceScraper {
  override def getService() = AdvertService.MyHomeIe

  override def getListPageScraper() = MyHomeIeListPageScraper
  override def getItemPageScraper() = MyHomeIeItemPageScraper
}
