package ie.nok.adverts.scraper.services.myhomeie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object MyHomeIeScraper extends ServiceScraper {
  override def getService() = AdvertService.MyHomeIe

  override def getInitialListPageUrl() = URL("https://www.myhome.ie/sitemaps/residentialforsale_sitemap.xml")
  override def getListPageScraper()    = MyHomeIeListPageScraper
  override def getItemPageScraper()    = MyHomeIeItemPageScraper
}
