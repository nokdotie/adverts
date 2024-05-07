package ie.nok.adverts.scraper.services.propertypalcom

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.{ServiceScraper, SitemapListPageScraper}

object PropertyPalComScraper extends ServiceScraper {
  override def getService() = AdvertService.PropertyPalCom

  override def getListPageScraper() = PropertyPalComListPageScraper
  override def getItemPageScraper() = PropertyPalComItemPageScraper
}
