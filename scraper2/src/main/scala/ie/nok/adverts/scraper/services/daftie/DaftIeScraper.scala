package ie.nok.adverts.scraper.services.daftie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object DaftIeScraper extends ServiceScraper {
  override def getService() = AdvertService.DaftIe

  override def getListPageScraper() = DaftIeListPageScraper
  override def getItemPageScraper() = DaftIeItemPageScraper
}
