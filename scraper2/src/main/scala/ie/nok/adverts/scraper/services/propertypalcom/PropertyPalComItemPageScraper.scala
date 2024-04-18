package ie.nok.adverts.scraper.services.propertypalcom

import org.jsoup.nodes.Document
import ie.nok.adverts.PropertyType
import ie.nok.adverts.scraper.jsoup.JsoupHelper
import ie.nok.adverts.scraper.services.ServiceItemPageScraper
import ie.nok.adverts.AdvertSaleStatus
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import ie.nok.ber.Rating
import scala.util.chaining.scalaUtilChainingOps

object PropertyPalComItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean = true

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    AdvertSaleStatus.ForSale

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".pp-key-info-price b", raw"€([\d,]+)".r)
      .flatMap(_.group(1).filter(_.isDigit).toIntOption)
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, ".pp-property-description")

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .findRegex(document, ".pp-key-info-row", raw"Style (.+)".r)
      .map { _.group(1) }
      .map {
        case style if style.startsWith("Detached")      => PropertyType.Detached
        case style if style.startsWith("Terrace")       => PropertyType.Terraced
        case style if style.startsWith("End-terrace")   => PropertyType.Terraced
        case style if style.startsWith("Mid-terrace")   => PropertyType.Terraced
        case style if style.startsWith("Link-detached") => PropertyType.SemiDetached
        case style if style.startsWith("Semi-detached") => PropertyType.SemiDetached
        case style if style.contains("Apartment")       => PropertyType.Apartment
        case "Bungalow"                                 => PropertyType.Bungalow
        case "Chalet"                                   => PropertyType.House
        case "Chalet Bungalow"                          => PropertyType.Bungalow
        case "Cottage"                                  => PropertyType.House
        case "Country Estate"                           => PropertyType.House
        case "Country House"                            => PropertyType.House
        case "Flat"                                     => PropertyType.Apartment
        case "Holiday Home"                             => PropertyType.House
        case "House"                                    => PropertyType.House
        case "House and Land"                           => PropertyType.House
        case "Mid Townhouse"                            => PropertyType.House
        case "Retirement Home"                          => PropertyType.House
        case "Townhouse"                                => PropertyType.House
        case other                                      => throw new Exception(s"Unknown property type: $other, ${document.location}")
      }

  override def getAddress(document: Document): String = {
    val address = JsoupHelper
      .findString(document, "h1")
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }

    val county = JsoupHelper
      .findString(document, "h1 + p")
      .map { Eircode.unzip(_)._1 }
      .getOrElse("")

    s"$address $county".trim
  }

  override def getEircode(document: Document): Option[Eircode] =
    JsoupHelper
      .filterStrings(document, "h1 + p")
      .flatMap { Eircode.findFirstIn }
      .headOption

  override def getCoordinates(document: Document): Coordinates =
    Coordinates.zero

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper.filterAttributesSrc(document, ".pp-details-page > section img").distinct

  override def getSize(document: Document): Area =
    JsoupHelper
      .findRegex(document, ".pp-key-info-row", raw"Size ([\d\.]+) sq. metres".r)
      .headOption
      .map { _.group(1) }
      .fold(Area.zero) { size => Area(BigDecimal(size), AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".pp-key-info-row", raw"Bedrooms (\d+)".r)
      .headOption
      .flatMap { _.group(1).toIntOption }
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".pp-key-info-row", raw"Bathrooms (\d+)".r)
      .headOption
      .flatMap { _.group(1).toIntOption }
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findAttributeAlt(document, ".pp-key-info-row img")
      .flatMap { raw"BER rating ([\w]+)".r.findFirstMatchIn }
      .map { _.group(1) }
      .flatMap { value =>
        Rating
          .tryFromString(value)
          .toOption
          .orElse { throw new Exception(s"Unknown rating: $value, ${document.location}") }
      }

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    None

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    None

  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String] =
    None

}
