package ie.nok.adverts.scraper.services.daftie

import org.jsoup.nodes.Document
import ie.nok.adverts.PropertyType
import ie.nok.adverts.scraper.services.JsoupHelper
import ie.nok.adverts.scraper.services.ServiceItemPageScraper
import ie.nok.adverts.AdvertSaleStatus
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import ie.nok.ber.Rating
import scala.util.chaining.scalaUtilChainingOps

object DaftIeItemPageScraper extends ServiceItemPageScraper {

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findString(document, "[data-testid=price]")
      .exists { _.contains("SALE AGREED") }
      .pipe {
        case true  => AdvertSaleStatus.SaleAgreed
        case false => AdvertSaleStatus.ForSale
      }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findInt(document, "[data-testid=price]")
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper.findStringKeepLineBreaks(document, "[data-testid=description] [data-testid=description]")

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .findString(document, "[data-testid=property-type]")
      .flatMap {
        case "Apartment" => Some(PropertyType.Apartment)
        case "Terrace"   => Some(PropertyType.Terraced)
        case other =>
          println(s"Unknown property type: $other, ${document.baseUri}")
          None
      }

  override def getAddress(document: Document): String =
    JsoupHelper.findString(document, "[data-testid=address]").fold("") { Eircode.unzip(_)._1 }

  override def getEircode(document: Document): Option[Eircode] =
    JsoupHelper.findString(document, "[data-testid=address]").flatMap { Eircode.unzip(_)._2 }

  private val coordinatesRegex = raw"https:\/\/www\.google\.com\/maps\/@\?api=1&map_action=pano&viewpoint=(-?\d+\.?\d+),(-?\d+\.?\d+)".r
  override def getCoordinates(document: Document): Coordinates =
    JsoupHelper
      .findAttributeHref(document, "[data-testid=streetview-button]")
      .flatMap { coordinatesRegex.findFirstMatchIn }
      .map { coordinates =>
        val latitude  = coordinates.group(1).pipe { BigDecimal(_) }
        val longitude = coordinates.group(2).pipe { BigDecimal(_) }

        Coordinates(latitude = latitude, longitude = longitude)
      }
      .getOrElse(Coordinates.zero)

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper.filterAttributesSrc(document, "[data-testid=main-header-image], [data-testid^=extra-header-image-]")

  override def getSize(document: Document): Area =
    JsoupHelper
      .findInt(document, "[data-testid=floor-area]")
      .fold(Area.zero) { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findInt(document, "[data-testid=beds]")
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findInt(document, "[data-testid=baths]")
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper.findAttributeAlt(document, "[data-testid=ber] > img").flatMap { Rating.tryFromString(_).toOption }

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    JsoupHelper
      .findInt(document, "[data-testid=ber-code]")

  private val berEnergyRatingRegex = raw"(\d+\.?\d*) kWh/m2/yr".r
  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    JsoupHelper
      .findString(document, "[data-testid=ber-epi] > span:nth-child(2)")
      .flatMap { berEnergyRatingRegex.findFirstMatchIn }
      .map { _.group(1) }
      .map { BigDecimal(_) }

  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String] =
    None

}
