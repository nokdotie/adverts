package ie.nok.adverts.scraper.services.abbeypropertysalescom

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object AbbeyPropertySalesComScraper extends ServiceScraper {
  override def getService() = AdvertService.AbbeyPropertySalesCom

  override def getInitialListPageUrl() = AbbeyPropertySalesComListPageScraper.getUrls().head
  override def getListPageScraper()    = AbbeyPropertySalesComListPageScraper
  override def getItemPageScraper()    = AbbeyPropertySalesComItemPageScraper
}
