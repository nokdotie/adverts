package ie.nok.adverts.scraper.services.allenandjacobsie

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

object AllenAndJacobsIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    JsoupHelper
      .findString(document, ".listing-header-item:has(.fa-th)")
      .map { _.trim }
      .pipe {
        case Some("Sale Agreed") => true
        case Some("For Sale")    => true
        case Some("Sold")        => true
        case _                   => false
      }

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findString(document, ".listing-header-item:has(.fa-th)")
      .map { _.trim }
      .pipe {
        case Some("Sale Agreed") => AdvertSaleStatus.SaleAgreed
        case Some("For Sale")    => AdvertSaleStatus.ForSale
        case Some("Sold")        => AdvertSaleStatus.Sold
        case other               => throw new Exception(s"Unknown sale status: $other, ${document.location}")
      }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".row", raw"Asking Price : €([\d,]+)".r)
      .map { _.group(1) }
      .flatMap { _.filter(_.isDigit).toIntOption }
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, ".Property-Details-section-title:has(.fa-align-justify) ~ p")
      .orElse { throw new Exception(s"Description not found: ${document.location}") }

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .findString(document, ".listing-header-item:has(.fa-home)")
      .flatMap {
        case "Apartment"       => Some(PropertyType.Apartment)
        case "House"           => Some(PropertyType.House)
        case "Site-individual" => Some(PropertyType.Site)
        case "Other"           => None
        case other             => throw new Exception(s"Unknown property type: $other, ${document.location}")
      }

  override def getAddress(document: Document): String =
    JsoupHelper
      .findString(document, "h2")
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }

  override def getEircode(document: Document): Option[Eircode] =
    None

  override def getCoordinates(document: Document): Coordinates =
    JsoupHelper
      .find(document, ".Property-Details-section-title + script")
      .map { _.data }
      .flatMap { raw""""Latitude":(-?\d+\.\d+),"Longitude":(-?\d+\.\d+)""".r.findFirstMatchIn }
      .map { found => (BigDecimal(found.group(1)), BigDecimal(found.group(2))) }
      .map { case (latitude, longitude) =>
        Coordinates(
          latitude = latitude,
          longitude = longitude
        )
      }
      .getOrElse(Coordinates.zero)

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper
      .filterAttributesSrc(document, "#imageGallery img")

  override def getSize(document: Document): Area =
    JsoupHelper
      .findRegex(document, ".listing-header-item:has(.fa-arrows-alt)", raw"(\d+) sq.m.".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }
      .fold(Area.zero) { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findString(document, ".listing-header-item:has(.fa-bed)")
      .flatMap { _.toIntOption }
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    0

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findAttributeClass(document, ".ber1")
      .fold(List.empty) { _.split(" ").toList }
      .filter { _ != "ber1" }
      .flatMap {
        case "Exempt" => None
        case str =>
          Rating
            .tryFromString(str)
            .toOption
            .orElse { throw new Exception(s"Unknown BER rating: $str, ${document.location}") }
      }
      .headOption

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    JsoupHelper
      .findRegex(document, "p", raw"BERNo. (\d+)".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    JsoupHelper
      .findRegex(document, "p", raw"EPI: ([\d.]+) kWh/m2/yr".r)
      .map { _.group(1) }
      .filter { _ != "0" }
      .map { BigDecimal(_) }

  override def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String] =
    None

}
