package ie.nok.adverts.scraper.services.alanbrowneestatesie

import java.net.URL
import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

object AlanBrowneEstatesIeScraper extends ServiceScraper {
  override def getService() = AdvertService.AlanBrowneEstatesIe

  override def getListPageScraper() = AlanBrowneEstatesIeListPageScraper
  override def getItemPageScraper() = AlanBrowneEstatesIeItemPageScraper
}
