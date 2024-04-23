package ie.nok.adverts.scraper.services.jordancsie

import ie.nok.adverts.scraper.services.ServiceItemPageScraper
import ie.nok.adverts.{AdvertSaleStatus, PropertyType}
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.Area
import org.jsoup.nodes.Document

object JordanCsIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean = ???

  override def getSaleStatus(document: Document): AdvertSaleStatus                                              = ???
  override def getPriceInEur(document: Document): Int                                                           = ???
  override def getDescription(document: Document): Option[String]                                               = ???
  override def getPropertyType(document: Document): Option[PropertyType]                                        = ???
  override def getAddress(document: Document): String                                                           = ???
  override def getEircode(document: Document): Option[Eircode]                                                  = ???
  override def getCoordinates(document: Document): Coordinates                                                  = ???
  override def getImageUrls(document: Document): List[String]                                                   = ???
  override def getSize(document: Document): Area                                                                = ???
  override def getBedroomsCount(document: Document): Int                                                        = ???
  override def getBathroomsCount(document: Document): Int                                                       = ???
  override def getBuildingEnergyRating(document: Document): Option[Rating]                                      = ???
  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int]                        = ???
  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] = ???
  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String]                     = ???
}
