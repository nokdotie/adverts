package ie.nok.adverts.scraper.services.abbeypropertysalescom

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

object AbbeyPropertySalesComItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    getPropertyType(document).isDefined

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findString(document, ".property-price")
      .pipe {
        case Some(status) if status.contains("SOLD")        => AdvertSaleStatus.Sold
        case Some(status) if status.contains("SALE AGREED") => AdvertSaleStatus.SaleAgreed
        case _                                              => AdvertSaleStatus.ForSale
      }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".property-price", raw"€ ([\d,]+)".r)
      .map { _.group(1) }
      .flatMap { _.filter(_.isDigit).toIntOption }
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, ".property-description")
      .map { _.linesIterator.drop(1).mkString("\n") }
      .orElse { throw new Exception(s"Description not found: ${document.location}") }

  private def getPropertyOverview(document: Document, property: String): List[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, ".property-overview")
      .getOrElse("")
      .linesIterator
      .filter { _.startsWith(property) }
      .map { _.replaceFirst(property, "") }
      .toList

  override def getPropertyType(document: Document): Option[PropertyType] =
    getPropertyOverview(document, "Type").flatMap {
      case t if t.contains("Country Home")        => Some(PropertyType.House)
      case t if t.contains("Detached House")      => Some(PropertyType.Detached)
      case t if t.contains("Semi-detached House") => Some(PropertyType.SemiDetached)
      case "Apartment"                            => Some(PropertyType.Apartment)
      case "Bungalow"                             => Some(PropertyType.Bungalow)
      case "Cottage"                              => Some(PropertyType.House)
      case "Farm"                                 => Some(PropertyType.Site)
      case "Site"                                 => Some(PropertyType.Site)
      case "Terraced"                             => Some(PropertyType.Terraced)
      case "Residential"                          => None
      case "Sites"                                => None
      case "Farms"                                => None
      case "Commercial"                           => None
      case "Bar/Restaurant/Nightclub"             => None
      case "Forestry"                             => None
      case "Investment Property"                  => None
      case other                                  => throw new Exception(s"Unknown property type: $other, ${document.location}")
    }.headOption

  private def getAddressAndEircode(document: Document): (String, Option[Eircode]) =
    JsoupHelper
      .findString(document, "h1")
      .map { Eircode.unzip(_) }
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }

  override def getAddress(document: Document): String =
    getAddressAndEircode(document)._1

  override def getEircode(document: Document): Option[Eircode] =
    getAddressAndEircode(document)._2

  override def getCoordinates(document: Document): Coordinates =
    Coordinates.zero

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper
      .filterAttributesHref(document, ".property-detail-gallery a")

  override def getSize(document: Document): Area =
    getPropertyOverview(document, "Home area")
      .pipe {
        case list if list.isEmpty => getPropertyOverview(document, "Site Size")
        case list                 => list
      }
      .flatMap { raw"([\d,]+) (.*)".r.findFirstMatchIn(_) }
      .map { found => (found.group(1).filter { _.isDigit }, found.group(2)) }
      .map {
        case (value, "sqft")  => Area(BigDecimal(value), AreaUnit.SquareFeet)
        case (value, "acres") => Area(BigDecimal(value), AreaUnit.Acres)
        case (value, other)   => throw new Exception(s"Unknown unit: $other, ${document.location}")
      }
      .headOption
      .getOrElse(Area.zero)

  override def getBedroomsCount(document: Document): Int =
    getPropertyOverview(document, "Beds")
      .flatMap { _.toIntOption }
      .headOption
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    getPropertyOverview(document, "Baths")
      .flatMap { _.toIntOption }
      .headOption
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    None

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    None

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    None

  override def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String] =
    Some("001450")

}
