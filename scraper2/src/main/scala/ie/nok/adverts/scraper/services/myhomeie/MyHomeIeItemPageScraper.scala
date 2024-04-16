package ie.nok.adverts.scraper.services.myhomeie

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

object MyHomeIeItemPageScraper extends ServiceItemPageScraper {

  override def filter(document: Document): Boolean = true

  override def getSaleStatus(document: Document): AdvertSaleStatus =
    JsoupHelper
      .findString(document, ".brochure__price")
      .pipe {
        case Some("Sale Agreed") => AdvertSaleStatus.SaleAgreed
        case Some("Sold")        => AdvertSaleStatus.Sold
        case _                   => AdvertSaleStatus.ForSale
      }

  override def getPriceInEur(document: Document): Int =
    JsoupHelper
      .findRegex(document, ".brochure__price", raw"€([\d,]+)".r)
      .flatMap(_.group(1).filter(_.isDigit).toIntOption)
      .getOrElse(0)

  override def getDescription(document: Document): Option[String] =
    JsoupHelper
      .findStringKeepLineBreaks(document, ".brochure__details--description-content")
      .orElse { throw new Exception(s"Description not found: ${document.baseUri}") }

  override def getPropertyType(document: Document): Option[PropertyType] =
    None

  override def getAddress(document: Document): String =
    JsoupHelper
      .findString(document, "h4")
      .getOrElse { throw new Exception(s"Address not found: ${document.baseUri}") }

  override def getEircode(document: Document): Option[Eircode] =
    JsoupHelper
      .filterStrings(document, ".info-strip--divider")
      .flatMap { Eircode.findFirstIn }
      .headOption

  override def getCoordinates(document: Document): Coordinates =
    ServiceItemPageScraper
      .googleMapsCoordinates(document)
      .getOrElse { throw new Exception(s"Coordinates not found: ${document.baseUri}") }

  override def getImageUrls(document: Document): List[String] =
    JsoupHelper.filterAttributesSrc(document, ".gallery img").distinct

  override def getSize(document: Document): Area =
    JsoupHelper
      .filterRegex(document, ".info-strip--divider", raw"([\d\.]+) m2".r)
      .headOption
      .map { _.group(1) }
      .map { size => Area(BigDecimal(size), AreaUnit.SquareMetres) }
      .getOrElse { throw new Exception(s"Size not found: ${document.baseUri}") }

  override def getBedroomsCount(document: Document): Int =
    JsoupHelper
      .filterRegex(document, ".info-strip--divider", raw"([\d\.]+) beds".r)
      .headOption
      .flatMap { _.group(1).toIntOption }
      .getOrElse { throw new Exception(s"Bedrooms count not found: ${document.baseUri}") }

  override def getBathroomsCount(document: Document): Int =
    JsoupHelper
      .filterRegex(document, ".info-strip--divider", raw"([\d\.]+) baths".r)
      .headOption
      .flatMap { _.group(1).toIntOption }
      .getOrElse(0)

  override def getBuildingEnergyRating(document: Document): Option[Rating] =
    JsoupHelper
      .findRegex(document, ".brochure__details--description-content", raw"BER: (\w+)".r)
      .map { _.group(1) }
      .flatMap { value =>
        Rating
          .tryFromString(value)
          .toOption
          .orElse { throw new Exception(s"Unknown rating: $value, ${document.baseUri}") }
      }

  override def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int] = JsoupHelper
    .findRegex(document, ".brochure__details--description-content", raw"BER No: (\d+)".r)
    .flatMap { _.group(1).toIntOption }

  override def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal] =
    JsoupHelper
      .findRegex(document, ".brochure__details--description-content", raw"Energy Performance Indicator: (\d+)".r)
      .map { _.group(1) }
      .map { BigDecimal(_) }

  override def getPropertyServicesProviderLicenceNumber(document: Document): Option[String] =
    None

}
