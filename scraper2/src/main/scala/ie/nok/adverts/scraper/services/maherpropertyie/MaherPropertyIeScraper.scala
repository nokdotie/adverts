package ie.nok.adverts.scraper.services.maherpropertyie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object MaherPropertyIeScraper extends ServiceScraper {
  override def getService() = AdvertService.MaherPropertyIe

  override def getInitialListPageUrl() = URL("https://maherproperty.ie/property-sitemap.xml")
  override def getListPageScraper()    = MaherPropertyIeListPageScraper
  override def getItemPageScraper()    = MaherPropertyIeItemPageScraper
}
