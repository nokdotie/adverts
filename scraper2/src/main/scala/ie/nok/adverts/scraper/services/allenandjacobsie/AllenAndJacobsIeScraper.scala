package ie.nok.adverts.scraper.services.allenandjacobsie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object AllenAndJacobsIeScraper extends ServiceScraper {
  override def getService() = AdvertService.AllenAndJacobsIe

  override def getListPageScraper() = AllenAndJacobsIeListPageScraper
  override def getItemPageScraper() = AllenAndJacobsIeItemPageScraper
}
