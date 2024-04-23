package ie.nok.adverts.scraper.services

import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.daftie.DaftIeScraper
import ie.nok.adverts.scraper.services.maherpropertyie.MaherPropertyIeScraper
import ie.nok.adverts.scraper.services.myhomeie.MyHomeIeScraper
import ie.nok.adverts.scraper.services.propertypalcom.PropertyPalComScraper
import java.net.URL
import ie.nok.adverts.scraper.services.fivesie.FiveSIeScraper

trait ServiceScraper {
  def getService(): AdvertService

  def getInitialListPageUrl(): URL
  def getListPageScraper(): ServiceListPageScraper
  def getItemPageScraper(): ServiceItemPageScraper
}

object ServiceScraper {
  val all = List(
    DaftIeScraper,
    FiveSIeScraper,
    MaherPropertyIeScraper,
    MyHomeIeScraper,
    PropertyPalComScraper
  )
}
