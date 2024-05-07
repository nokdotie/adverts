package ie.nok.adverts.scraper.services.alanbrowneestatesie

import org.jsoup.nodes.Document
import ie.nok.adverts.PropertyType
import ie.nok.adverts.scraper.jsoup.JsoupHelper
import ie.nok.adverts.scraper.services.ServiceItemPageScraper
import ie.nok.adverts.AdvertSaleStatus
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import scala.util.chaining.scalaUtilChainingOps

object AlanBrowneEstatesIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    true

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    AdvertSaleStatus.ForSale

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, "#Price", raw"€([\d,]+)".r)
      .flatMap(_.group(1).filter(_.isDigit).toIntOption)
      .orElse { JsoupHelper.findRegex(document, "#Price", "Contact Us for Price".r).map { _ => 0 } }
      .getOrElse { throw new Exception(s"Price not found: ${document.location}") }

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, "#Details")
      .orElse { throw new Exception(s"Description not found: ${document.location}") }

  override def getPropertyType(document: Document): Option[PropertyType] =
    None

  private def getAddressAndEircode(document: Document): (String, Option[Eircode]) =
    JsoupHelper
      .findString(document, "#Address")
      .map(Eircode.unzip)
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }

  override def getAddress(document: Document): String =
    getAddressAndEircode(document)._1

  override def getEircode(document: Document): Option[Eircode] =
    getAddressAndEircode(document)._2

  override def getCoordinates(document: Document): Coordinates =
    Coordinates.zero

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper
      .filterAttributesSrc(document, "#Pictures img")

  override def getSize(document: Document): Area =
    JsoupHelper
      .findRegex(document, "#properties span", raw"(\d+)m²".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }
      .map { Area(_, AreaUnit.SquareMetres) }
      .getOrElse { throw new Exception(s"Size not found: ${document.location}") }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findString(document, "#properties .fa-bed + span")
      .flatMap { _.toIntOption }
      .getOrElse { throw new Exception(s"Bedrooms count not found: ${document.location}") }

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findString(document, "#properties .fa-bath + span")
      .flatMap { _.toIntOption }
      .getOrElse { throw new Exception(s"Bathrooms count not found: ${document.location}") }

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findAttributeClass(document, "#berCert .berRating")
      .fold(List.empty) { _.split(" ").toList }
      .filter { !List("berRating", "None").contains(_) }
      .map { str =>
        Rating
          .tryFromString(str)
          .getOrElse { throw new Exception(s"Unknown BER rating: $str, ${document.location}") }
      }
      .headOption

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    JsoupHelper
      .findRegex(document, "#BER", raw"(BER Number|BER Numbers|BER no|NO):? (\d+)".r)
      .map { _.group(2) }
      .flatMap { _.toIntOption }

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    None

  override def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String] =
    None

}
