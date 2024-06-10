package ie.nok.adverts.scraper.services.propertypartnersie

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

object PropertyPartnersIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    !JsoupHelper
      .findString(document, "h1.page-header")
      .contains("propertypartners.ie - Page Not Found - 404")

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findRegex(document, "h2", raw"(For Sale|Sale Agreed)".r)
      .map { _.group(1) }
      .pipe {
        case None                => AdvertSaleStatus.ForSale
        case Some("For Sale")    => AdvertSaleStatus.ForSale
        case Some("Sale Agreed") => AdvertSaleStatus.SaleAgreed
        case other               => throw new Exception(s"Unknown sale status: $other, ${document.location}")
      }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, "div > section > h1 span", raw"€([\d,]+)".r)
      .map { _.group(1) }
      .flatMap { _.filter(_.isDigit).toIntOption }
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, ".propertyContent p")

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .findString(document, "h2")
      .getOrElse("")
      .pipe {
        case str if str.contains("Bed") => str.split("Bed")(1)
        case str                        => str
      }
      .takeWhile { !_.isDigit }
      .replace("For Sale", "")
      .replace("Sale Agreed", "")
      .trim
      .pipe {
        case ""                                => None
        case "Apartment"                       => Some(PropertyType.Apartment)
        case "Bungalow"                        => Some(PropertyType.Bungalow)
        case "Cottage"                         => Some(PropertyType.Bungalow)
        case "Country House"                   => Some(PropertyType.House)
        case "Detached House"                  => Some(PropertyType.Detached)
        case "Development Land"                => Some(PropertyType.Site)
        case "Dormer"                          => Some(PropertyType.House)
        case "Duplex"                          => Some(PropertyType.Apartment)
        case "End of Terrace House"            => Some(PropertyType.EndOfTerrace)
        case "Farm"                            => Some(PropertyType.Site)
        case "Holiday Home"                    => Some(PropertyType.House)
        case "Residential Investment Property" => None
        case "Semi-Detached House"             => Some(PropertyType.SemiDetached)
        case "Site"                            => Some(PropertyType.Site)
        case "Terraced House"                  => Some(PropertyType.Terraced)
        case "Townhouse"                       => Some(PropertyType.House)
        case other =>
          println(s"Unknown property type: $other, ${document.location}")
          None
      }

  private def getAddressAndEircode(document: Document): (String, Option[Eircode]) =
    JsoupHelper
      .findString(document, "div > section > h1")
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }
      .takeWhile { _ != '€' }
      .replace("Sale Agreed", "")
      .pipe { Eircode.unzip }

  override def getAddress(document: Document): String =
    getAddressAndEircode(document)._1

  override def getEircode(document: Document): Option[Eircode] =
    getAddressAndEircode(document)._2

  override def getCoordinates(document: Document): Coordinates =
    JsoupHelper
      .findAttributeValue(document, "#propertyLat")
      .zip(JsoupHelper.findAttributeValue(document, "#propertyLng"))
      .fold(Coordinates.zero) { case (latitude, longitude) =>
        Coordinates(
          latitude = BigDecimal(latitude),
          longitude = BigDecimal(longitude)
        )
      }

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper
      .filterAttributesHref(document, ".imageGallery a.swipebox")

  override def getSize(document: Document): Area =
    JsoupHelper
      .findRegex(document, "h2", raw"([\d+\.?\d*]+) m²".r)
      .map { _.group(1) }
      .map { BigDecimal(_) }
      .fold(Area.zero) { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, "h2", raw"([\d+\.?\d*]+) Bed".r)
      .flatMap { _.group(1).toIntOption }
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int = 0

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findAttributeAlt(document, "div > section > h1 img")
      .flatMap {
        case "EXEMPT" => None
        case str =>
          Rating
            .tryFromString(str)
            .toOption
      }
      .headOption

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    JsoupHelper
      .findRegex(document, ".propertyContent", raw"BER No\.(\d+)".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    JsoupHelper
      .findRegex(document, ".propertyContent", raw"Energy Performance Indicator:(\d+\.?\d*) kWh/m²/yr".r)
      .map { _.group(1) }
      .map { BigDecimal(_) }

  override def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String] =
    None

}
