package ie.nok.adverts.scraper.services.realestateallianceie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.{ServiceScraper, SitemapListPageScraper}

object RealEstateAllianceIeScraper extends ServiceScraper {
  override def getService() = AdvertService.RealEstateAllianceIe

  override def getListPageScraper() = RealEstateAllianceIeListPageScraper
  override def getItemPageScraper() = RealEstateAllianceIeItemPageScraper
}
