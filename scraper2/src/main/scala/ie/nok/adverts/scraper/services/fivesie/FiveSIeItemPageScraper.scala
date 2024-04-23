package ie.nok.adverts.scraper.services.fivesie

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

object FiveSIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    JsoupHelper
      .findString(document, "#detail .prop_status")
      .exists { _.contains("For Sale") }

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findString(document, "#detail .prop_status")
      .getOrElse("")
      .pipe {
        case status if status.contains("SALE AGREED") => AdvertSaleStatus.SaleAgreed
        case _                                        => AdvertSaleStatus.ForSale
      }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".item-price", raw"€([\d,]+)".r)
      .flatMap(_.group(1).filter(_.isDigit).toIntOption)
      .getOrElse { throw new Exception(s"Price not found: ${document.baseUri}") }

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, "#description")
      .orElse { throw new Exception(s"Description not found: ${document.baseUri}") }

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .findRegex(document, "#detail .prop_type", raw"Property Type: (.+)".r)
      .map { _.group(1) }
      .map {
        case t if t.startsWith("Apartment,") => PropertyType.Apartment
        case other                           => throw new Exception(s"Unknown property type: $other, ${document.baseUri}")
      }
      .orElse { throw new Exception(s"Property type not found: ${document.baseUri}") }

  override def getAddress(document: Document): String = {
    val address = JsoupHelper
      .findString(document, ".property-address")
      .getOrElse { throw new Exception(s"Address not found: ${document.baseUri}") }

    val detailAddressPart = JsoupHelper
      .findRegex(document, "#address .detail-address", raw"Address: (.+)".r)
      .map { _.group(1) }

    val detailCityPart = JsoupHelper
      .findRegex(document, "#address .detail-city", raw"City: (.+)".r)
      .map { _.group(1) }

    val detailAddress = List(detailAddressPart, detailCityPart)

    detailAddress.pipe {
      case detailAddress if detailAddress.contains(None) => address
      case detailAddress                                 => detailAddress.flatten.mkString(", ")
    }
  }

  override def getEircode(document: Document): Option[Eircode] =
    JsoupHelper
      .findString(document, "#address .detail-zip")
      .orElse(JsoupHelper.findString(document, ".property-address"))
      .flatMap(Eircode.findFirstIn)

  override def getCoordinates(document: Document): Coordinates =
    Coordinates.zero

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper
      .filterAttributesStyleBackgroundImage(document, "#gallery .owl-carousel .item")

  override def getSize(document: Document): Area =
    JsoupHelper
      .findRegex(document, "#detail", raw"Property Size: (\d+)".r)
      .map { _.group(1) }
      .map { BigDecimal(_) }
      .fold(Area.zero) { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, "#detail", raw"Bedrooms: (\d+)".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }
      .getOrElse { throw new Exception(s"Bedrooms count not found: ${document.baseUri}") }

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, "#detail", raw"Bathrooms: (\d+)".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }
      .getOrElse { throw new Exception(s"Bathrooms count not found: ${document.baseUri}") }

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    None

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    None

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    None

  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String] =
    None

}
