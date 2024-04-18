package ie.nok.adverts.scraper.services.daftie

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

object DaftIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    JsoupHelper
      .findString(document, "[data-testid=back-button]")
      .nonEmpty

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
      .findRegex(document, "[data-testid=price]", raw"€([\d,]+)".r)
      .flatMap(_.group(1).filter(_.isDigit).toIntOption)
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, "[data-testid=description] [data-testid=description]")
      .orElse { throw new Exception(s"Description not found: ${document.location}") }

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .findString(document, "[data-testid=property-type]")
      .flatMap {
        case "Apartment"      => Some(PropertyType.Apartment)
        case "Bungalow"       => Some(PropertyType.Bungalow)
        case "Detached"       => Some(PropertyType.Detached)
        case "Duplex"         => Some(PropertyType.Duplex)
        case "End of Terrace" => Some(PropertyType.EndOfTerrace)
        case "House"          => Some(PropertyType.House)
        case "Houses"         => Some(PropertyType.House)
        case "Property"       => None
        case "Semi-D"         => Some(PropertyType.SemiDetached)
        case "Site"           => Some(PropertyType.Site)
        case "Studio"         => Some(PropertyType.Studio)
        case "Terrace"        => Some(PropertyType.Terraced)
        case "Townhouse"      => Some(PropertyType.House)
        case other            => throw new Exception(s"Unknown property type: $other, ${document.location}")
      }

  override def getAddress(document: Document): String =
    JsoupHelper
      .findString(document, "[data-testid=address]")
      .orElse(JsoupHelper.findString(document, "[data-testid=alt-title]"))
      .map { Eircode.unzip(_)._1 }
      .getOrElse { throw new Exception(s"Address not found: ${document.location}") }

  override def getEircode(document: Document): Option[Eircode] =
    JsoupHelper.findString(document, "[data-testid=address]").flatMap { Eircode.unzip(_)._2 }

  private val coordinatesRegex = raw"https:\/\/www\.google\.com\/maps\/@\?api=1&map_action=pano&viewpoint=(-?\d+\.?\d+),(-?\d+\.?\d+)".r
  override def getCoordinates(document: Document): Coordinates =
    ServiceItemPageScraper
      .googleMapsCoordinates(document)
      .getOrElse { throw new Exception(s"Coordinates not found: ${document.location}") }

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper.filterAttributesSrc(document, "[data-testid=main-header-image], [data-testid^=extra-header-image-]")

  private val sizeAcresRegex  = raw"([\d\.]+) ac".r
  private val sizeSqrMtrRegex = raw"([\d\.]+) m²".r
  override def getSize(document: Document): Area =
    JsoupHelper
      .findString(document, "[data-testid=floor-area]")
      .map {
        case sizeAcresRegex(size)  => Area(BigDecimal(size), AreaUnit.Acres)
        case sizeSqrMtrRegex(size) => Area(BigDecimal(size), AreaUnit.SquareMetres)
        case other                 => throw new Exception(s"Unknown size: $other, ${document.location}")
      }
      .getOrElse(Area.zero)

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, "[data-testid=beds]", raw"(\d+) Bed".r)
      .flatMap { _.group(1).toIntOption }
      .getOrElse(0)

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, "[data-testid=baths]", raw"(\d+) Bath".r)
      .flatMap { _.group(1).toIntOption }
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findAttributeAlt(document, "[data-testid=ber] > img")
      .flatMap {
        case ""       => None
        case "NA"     => None
        case "SI_666" => None
        case other =>
          Rating
            .tryFromString(other)
            .toOption
            .orElse {
              println(s"Unknown BER rating: $other, ${document.location}")
              None
            }
      }

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    JsoupHelper
      .findRegex(document, "[data-testid=ber-code]", raw"BER No: (\d+)".r)
      .flatMap { _.group(1).toIntOption }

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    JsoupHelper
      .findRegex(document, "[data-testid=ber-epi] > span:nth-child(2)", raw"(\d+\.?\d*) kWh/m2/yr".r)
      .map { _.group(1) }
      .map { BigDecimal(_) }

  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String] =
    None

}
