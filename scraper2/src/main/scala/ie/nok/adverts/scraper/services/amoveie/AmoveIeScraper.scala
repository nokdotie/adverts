package ie.nok.adverts.scraper.services.amoveie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object AmoveIeScraper extends ServiceScraper {
  override def getService() = AdvertService.AmoveIe

  override def getListPageScraper() = AmoveIeListPageScraper
  override def getItemPageScraper() = AmoveIeItemPageScraper
}
