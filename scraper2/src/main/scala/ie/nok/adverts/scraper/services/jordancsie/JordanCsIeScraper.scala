package ie.nok.adverts.scraper.services.jordancsie

import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.ServiceScraper

import java.net.URL

object JordanCsIeScraper extends ServiceScraper {
  override def getService() = AdvertService.JordanCsIe

  override def getInitialListPageUrl() = JordanCsIeListPageScraper.getUrls().head
  override def getListPageScraper()    = JordanCsIeListPageScraper
  override def getItemPageScraper()    = JordanCsIeItemPageScraper
}
