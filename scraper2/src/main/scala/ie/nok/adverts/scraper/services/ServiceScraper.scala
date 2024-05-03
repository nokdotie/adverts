package ie.nok.adverts.scraper.services

import ie.nok.adverts.AdvertService
import ie.nok.adverts.scraper.services.abbeypropertysalescom.AbbeyPropertySalesComScraper
import ie.nok.adverts.scraper.services.amoveie.AmoveIeScraper
import ie.nok.adverts.scraper.services.daftie.DaftIeScraper
import ie.nok.adverts.scraper.services.fivesie.FiveSIeScraper
import ie.nok.adverts.scraper.services.maherpropertyie.MaherPropertyIeScraper
import ie.nok.adverts.scraper.services.myhomeie.MyHomeIeScraper
import ie.nok.adverts.scraper.services.propertypalcom.PropertyPalComScraper
import java.net.URL

trait ServiceScraper {
  def getService(): AdvertService

  def getInitialListPageUrl(): URL
  def getListPageScraper(): ServiceListPageScraper
  def getItemPageScraper(): ServiceItemPageScraper
}

object ServiceScraper {
  val all = List(
    AbbeyPropertySalesComScraper,
    AmoveIeScraper,
    DaftIeScraper,
    FiveSIeScraper,
    MaherPropertyIeScraper,
    MyHomeIeScraper,
    PropertyPalComScraper
  )
}
