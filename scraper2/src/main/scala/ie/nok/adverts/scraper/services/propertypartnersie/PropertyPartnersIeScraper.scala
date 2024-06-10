package ie.nok.adverts.scraper.services.propertypartnersie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object PropertyPartnersIeScraper extends ServiceScraper {
  override def getService() = AdvertService.PropertyPartnersIe

  override def getListPageScraper() = PropertyPartnersIeListPageScraper
  override def getItemPageScraper() = PropertyPartnersIeItemPageScraper
}
