package ie.nok.adverts.scraper.services

import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.daftie.DaftIeScraper
import ie.nok.adverts.scraper.services.maherpropertyie.MaherPropertyIeScraper
import java.net.URL

trait ServiceScraper {
  def getService(): AdvertService

  def getInitialListPageUrl(): URL
  def getListPageScraper(): ServiceListPageScraper
  def getItemPageScraper(): ServiceItemPageScraper
}

object ServiceScraper {
  val all = List(
    DaftIeScraper,
    MaherPropertyIeScraper
  )
}
