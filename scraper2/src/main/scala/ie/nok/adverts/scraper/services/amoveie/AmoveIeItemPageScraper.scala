package ie.nok.adverts.scraper.services.amoveie

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

object AmoveIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    JsoupHelper
      .findString(document, ".label-sale")
      .exists { _.contains("For Sale") }

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findString(document, "h1 font")
      .getOrElse("")
      .pipe {
        case status if status.contains("SOLD")        => AdvertSaleStatus.Sold
        case status if status.contains("SALE AGREED") => AdvertSaleStatus.SaleAgreed
        case _                                        => AdvertSaleStatus.ForSale
      }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findString(document, ".listing-price-value")
      .flatMap { _.filter(_.isDigit).toIntOption }
      .orElse { JsoupHelper.findString(document, ".listing-price-on-request").map { _ => 0 } }
      .getOrElse { throw new Exception(s"Price not found: ${document.location}") }

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, "div[itemprop=description]")
      .map { _.linesIterator.drop(1).mkString("\n") }
      .orElse { throw new Exception(s"Description not found: ${document.location}") }

  override def getPropertyType(document: Document): Option[PropertyType] =
    None

  private def getAddressAndEircode(document: Document): (String, Option[Eircode]) =
    JsoupHelper
      .findString(document, "h1")
      .map { _.replaceAll("SALE AGREED", "").replaceAll("SOLD", "").trim() }
      .map { Eircode.unzip(_) }
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }

  override def getAddress(document: Document): String =
    getAddressAndEircode(document)._1

  override def getEircode(document: Document): Option[Eircode] =
    getAddressAndEircode(document)._2

  override def getCoordinates(document: Document): Coordinates = {
    val latitude  = JsoupHelper.findAttributeContent(document, "meta[itemprop=latitude]")
    val longitude = JsoupHelper.findAttributeContent(document, "meta[itemprop=longitude]")

    latitude
      .zip(longitude)
      .map { case (lat, lon) => Coordinates(BigDecimal(lat), BigDecimal(lon)) }
      .getOrElse(Coordinates.zero)
  }

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper
      .filterAttributesHref(document, ".wpsight-gallery .wpsight-gallery-item a")

  override def getSize(document: Document): Area =
    JsoupHelper
      .findRegex(document, ".listing-details-detail", raw"Floor Area (\d+) m²".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }
      .fold(Area.zero) { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".listing-details-detail", raw"Beds (\d+)".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".listing-details-detail", raw"Baths (\d+)".r)
      .map { _.group(1) }
      .flatMap { _.toIntOption }
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findRegex(document, ".listing-details-detail", raw"BER (.+)".r)
      .map { _.group(1) }
      .flatMap {
        case "n/a" => None
        case value =>
          Rating
            .tryFromString(value)
            .toOption
            .orElse { throw new Exception(s"Unknown rating: $value, ${document.location}") }
      }

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    None

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    None

  override def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String] =
    None

}
