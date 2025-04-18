package ie.nok.adverts.scraper.services.maherpropertyie

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

object MaherPropertyIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean =
    JsoupHelper
      .findString(document, ".status, .status-label")
      .exists { _.startsWith("Residential Sales") }

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findString(document, ".rh_page__property_price .status")
      .collect {
        case "Residential Sales"              => AdvertSaleStatus.ForSale
        case "Residential Sales, Sale Agreed" => AdvertSaleStatus.SaleAgreed
        case "Residential Sales, Sold"        => AdvertSaleStatus.Sold
      }
      .getOrElse { throw new Exception(s"Status not found: ${document.location}") }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".price", raw"€([\d,]+)".r)
      .flatMap(_.group(1).filter(_.isDigit).toIntOption)
      .getOrElse { throw new Exception(s"Price not found: ${document.location}") }

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, "#property-content-section-content .rh_content")

  override def getPropertyType(document: Document): Option[PropertyType] =
    JsoupHelper
      .findString(document, ".price-and-type > small, .property-breadcrumbs li:last-of-type a")
      .map { _.replace("Residential", "").replace("Sale Agreed", "").filter(_.isLetter) }
      .pipe {
        case Some("")             => None
        case Some("Apartment")    => Some(PropertyType.Apartment)
        case Some("Bungalow")     => Some(PropertyType.Bungalow)
        case Some("Detached")     => Some(PropertyType.Detached)
        case Some("Duplex")       => Some(PropertyType.Duplex)
        case Some("EndofTerrace") => Some(PropertyType.EndOfTerrace)
        case Some("SemiDetached") => Some(PropertyType.SemiDetached)
        case other                => throw new Exception(s"Unknown property type: $other, ${document.location}")
      }

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
    JsoupHelper
      .find(document, "#property-open-street-map-js-extra")
      .map { _.data }
      .flatMap { raw""""lat":"(-?\d+\.\d+)","lng":"(-?\d+\.\d+)",""".r.findFirstMatchIn }
      .fold(Coordinates.zero) { m =>
        Coordinates(
          latitude = BigDecimal(m.group(1)),
          longitude = BigDecimal(m.group(2))
        )
      }

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper.filterAttributesHref(document, ".slides a")

  override def getSize(document: Document): Area =
    JsoupHelper
      .findString(document, ".prop_area .figure")
      .flatMap { _.trim.toIntOption }
      .fold(Area.zero) { Area(_, AreaUnit.SquareMetres) }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".property-meta-bedrooms", raw"(\d+) Bedrooms?".r)
      .map { _.group(1) }
      .orElse(JsoupHelper.findString(document, ".prop_bedrooms .figure"))
      .flatMap { _.toIntOption }
      .getOrElse { throw new Exception(s"Bedrooms count not found: ${document.location}") }

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".property-meta-bath", raw"(\d+) Bathrooms?".r)
      .map { _.group(1) }
      .orElse(JsoupHelper.findString(document, ".prop_bathrooms .figure"))
      .flatMap { _.toIntOption }
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findAttributeAlt(document, ".content img")
      .map { alt =>
        Rating
          .tryFromString(alt)
          .getOrElse { throw new Exception(s"Unknown BER rating: $alt, ${document.location}") }
      }

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] =
    JsoupHelper
      .findRegex(document, ".content p:nth-of-type(2)", raw"BER No: (\d+)".r)
      .flatMap { _.group(1).toIntOption }

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    JsoupHelper
      .findRegex(document, ".content p:nth-of-type(3)", raw"Energy Performance Indicator: (\d+\.?\d*) kWh/m2/yr".r)
      .orElse { JsoupHelper.findRegex(document, ".epc-details", raw"Energy Performance: (\d+\.?\d*)".r) }
      .map { _.group(1) }
      .map { BigDecimal(_) }

  override def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String] =
    Some("003837")

}
