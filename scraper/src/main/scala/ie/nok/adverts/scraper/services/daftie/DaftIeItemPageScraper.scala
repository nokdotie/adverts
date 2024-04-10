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
    AdvertSaleStatus.ForSale

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .selectFirstInt(document, "[data-testid=price]")
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    Some(JsoupHelper.selectFirstStringKeepNewLine(document, "[data-testid=description] [data-testid=description]"))

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .selectFirstString(document, "[data-testid=property-type]")
      .pipe {
        case "Terrace" => Some(PropertyType.Terraced)
        case other =>
          println(s"Unknown property type: $other, ${document.baseUri}")
          None
      }

  override def getAddress(document: Document): String =
    JsoupHelper.selectFirstString(document, "[data-testid=address]").pipe { Eircode.unzip }._1

  override def getEircode(document: Document): Option[Eircode] =
    JsoupHelper.selectFirstString(document, "[data-testid=address]").pipe { Eircode.unzip }._2

  private val coordinatesRegex = raw"https:\/\/www\.google\.com\/maps\/@\?api=1&map_action=pano&viewpoint=(-?\d+\.?\d+),(-?\d+\.?\d+)".r
  override def getCoordinates(document: Document): Coordinates =
    JsoupHelper
      .selectFirstHrefAttribute(document, "[data-testid=streetview-button]")
      .pipe { coordinatesRegex.findFirstMatchIn }
      .map { coordinates =>
        val latitude  = coordinates.group(1).pipe { BigDecimal(_) }
        val longitude = coordinates.group(2).pipe { BigDecimal(_) }

        Coordinates(latitude = latitude, longitude = longitude)
      }
      .getOrElse(Coordinates.zero)

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper.selectAllSrcAttributes(document, "[data-testid=main-header-image], [data-testid^=extra-header-image-]")

  override def getSize(document: Document): Area =
    JsoupHelper
      .selectFirstInt(document, "[data-testid=floor-area]")
      .getOrElse(0)
      .pipe { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .selectFirstInt(document, "[data-testid=beds]")
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .selectFirstInt(document, "[data-testid=baths]")
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper.selectFirstAltAttribute(document, "[data-testid=ber] > img").pipe { Rating.tryFromString }.toOption

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    None

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    None

  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String] =
    None

}
