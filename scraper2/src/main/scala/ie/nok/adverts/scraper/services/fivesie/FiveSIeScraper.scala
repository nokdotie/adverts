package ie.nok.adverts.scraper.services.fivesie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object FiveSIeScraper extends ServiceScraper {
  override def getService() = AdvertService.FiveSIe

  override def getInitialListPageUrl() = FiveSIeListPageScraper.getUrls().head
  override def getListPageScraper()    = FiveSIeListPageScraper
  override def getItemPageScraper()    = FiveSIeItemPageScraper
}
