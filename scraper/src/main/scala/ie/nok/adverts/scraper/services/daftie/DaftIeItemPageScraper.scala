package ie.nok.adverts.scraper.services.daftie

import org.jsoup.nodes.Document
import ie.nok.adverts.PropertyType
import ie.nok.adverts.scraper.services.ServiceItemPageScraper
import ie.nok.adverts.AdvertSaleStatus
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.Area
import ie.nok.ber.Rating
import scala.util.chaining.scalaUtilChainingOps

object DaftIeItemPageScraper extends ServiceItemPageScraper {

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    AdvertSaleStatus.ForSale

  override def getPriceInEur(document: Document): Int =
    0

  override def getDescription(document: Document): Option[String] =
    None

  override def getPropertyType(document: Document): Option[PropertyType] =
    None

  override def getAddress(document: Document): String =
    ""

  override def getEircode(document: Document): Option[Eircode] =
    None

  override def getCoordinates(document: Document): Coordinates =
    Coordinates.zero

  override def getImageUrls(document: Document): List[String] =
    List()

  override def getSize(document: Document): Area =
    Area.zero

  override def getBedroomsCount(document: Document): Int =
    0

  override def getBathroomsCount(document: Document): Int =
    0

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    None

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    None

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    None

  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String] =
    None

}
