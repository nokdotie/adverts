package ie.nok.adverts.scraper.services.realestateallianceie

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

object RealEstateAllianceIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean = true

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    AdvertSaleStatus.ForSale

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".Price--single .Price-priceValue", raw"€([\d,]+)".r)
      .flatMap(_.group(1).filter(_.isDigit).toIntOption)
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, ".ListingDescr-text")

  override def getPropertyType(document: Document): Option[PropertyType] =
    None

  override def getAddress(document: Document): String =
    JsoupHelper
      .findStringKeepLineBreaks(document, "h1.Address")
      .map { Eircode.unzip(_)._1 }
      .map { _.split(Array('\n', ',')).map { _.trim() }.filter { _.nonEmpty }.mkString(", ") }
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }

  override def getEircode(document: Document): Option[Eircode] =
    JsoupHelper
      .filterStrings(document, "h1.Address .Address-addressPostcode")
      .flatMap { Eircode.findFirstIn }
      .headOption

  override def getCoordinates(document: Document): Coordinates =
    JsoupHelper
      .findAttributeContent(document, "meta[property=og:latitude]")
      .zip(JsoupHelper.findAttributeContent(document, "meta[property=og:longitude]"))
      .fold(Coordinates.zero) { (latitude, longitude) =>
        Coordinates(
          latitude = BigDecimal(latitude),
          longitude = BigDecimal(longitude)
        )
      }

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper.filterAttributesHref(document, ".Slideshow-thumbs a").distinct

  override def getSize(document: Document): Area =
    JsoupHelper
      .findString(document, ".ListingFeatures-size .ListingFeatures-figure")
      .flatMap { _.filter(_.isDigit).toIntOption }
      .fold(Area.zero) { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findString(document, ".ListingFeatures-bedrooms .ListingFeatures-figure")
      .flatMap { _.toIntOption }
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findString(document, ".ListingFeatures-bathrooms .ListingFeatures-figure")
      .flatMap { _.toIntOption }
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findAttributeClass(document, ".EngergyRating.EngergyRating-ber")
      .fold(List.empty) { _.split(' ').toList }
      .map { _.replaceAll("EngergyRating", "") }
      .map { _.replaceAll("-ber", "") }
      .filter { _.nonEmpty }
      .headOption
      .flatMap { value =>
        Rating
          .tryFromString(value)
          .toOption
      }

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    JsoupHelper
      .findRegex(document, ".BerDetails", raw"BER No\.: (\d+)".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    JsoupHelper
      .findRegex(document, ".BerDetails", raw"Energy Performance Indicator: (\d+\.?\d*) kWh/m²/yr".r)
      .map { _.group(1) }
      .map { BigDecimal(_) }

  override def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String] =
    None

}
