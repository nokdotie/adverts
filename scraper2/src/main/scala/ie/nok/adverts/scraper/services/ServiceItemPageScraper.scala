package ie.nok.adverts.scraper.services

import ie.nok.adverts.{Advert, AdvertSaleStatus, PropertyType}
import ie.nok.adverts.scraper.jsoup.JsoupHelper
import ie.nok.advertisers.Advertiser
import ie.nok.advertisers.stores.AdvertiserStoreInMemory
import ie.nok.ber.Rating
import ie.nok.codecs.hash.Hash
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.Area
import java.net.URL
import java.time.Instant
import org.jsoup.nodes.Document
import scala.util.chaining.scalaUtilChainingOps
import ie.nok.adverts.AdvertFacet

trait ServiceItemPageScraper {

  def filter(document: Document): Boolean

  protected def getSaleStatus(document: Document): AdvertSaleStatus
  protected def getPriceInEur(document: Document): Int
  protected def getDescription(document: Document): Option[String]
  protected def getPropertyType(document: Document): Option[PropertyType]
  protected def getAddress(document: Document): String
  protected def getEircode(document: Document): Option[Eircode]
  protected def getCoordinates(document: Document): Coordinates
  protected def getImageUrls(document: Document): List[String]
  protected def getSize(document: Document): Area
  protected def getBedroomsCount(document: Document): Int
  protected def getBathroomsCount(document: Document): Int
  protected def getBuildingEnergyRating(document: Document): Option[Rating]
  protected def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int]
  protected def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal]
  protected def getPropertyServicesRegulatoryAuthorityLicenceNumber(document: Document): Option[String]

  private def getAdvertiser(document: Document): Option[Advertiser] =
    getPropertyServicesRegulatoryAuthorityLicenceNumber(document)
      .flatMap { propertyServicesRegulatoryAuthorityLicenceNumber =>
        AdvertiserStoreInMemory.all
          .find { _.propertyServicesRegulatoryAuthorityLicenceNumber == propertyServicesRegulatoryAuthorityLicenceNumber }
      }

  def getAdvert(document: Document): Advert = {
    val address = getAddress(document)
    val size    = getSize(document)

    Advert(
      advertUrl = document.location(),
      advertSaleStatus = getSaleStatus(document),
      advertPriceInEur = getPriceInEur(document),
      propertyIdentifier = Hash.encode(address),
      propertyDescription = getDescription(document),
      propertyType = getPropertyType(document),
      propertyAddress = address,
      propertyEircode = getEircode(document),
      propertyCoordinates = getCoordinates(document),
      propertyImageUrls = getImageUrls(document),
      propertySize = size,
      propertySizeInSqtMtr = Area.toSquareMetres(size).value,
      propertyBedroomsCount = getBedroomsCount(document),
      propertyBathroomsCount = getBathroomsCount(document),
      propertyBuildingEnergyRating = getBuildingEnergyRating(document),
      propertyBuildingEnergyRatingCertificateNumber = getBuildingEnergyRatingCertificateNumber(document),
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document),
      sources = List(),
      facets = List(AdvertFacet(document.location())),
      advertiser = getAdvertiser(document),
      createdAt = Instant.now()
    )
  }

}

object ServiceItemPageScraper {

  private val googleMapsCssQuery = "a[href^=https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=]"
  private val googleMapsRegex    = raw"https:\/\/www\.google\.com\/maps\/@\?api=1&map_action=pano&viewpoint=(-?\d+\.?\d+),(-?\d+\.?\d+)".r

  def googleMapsCoordinates(document: Document): Option[Coordinates] =
    JsoupHelper
      .findAttributeHref(document, googleMapsCssQuery)
      .flatMap { googleMapsRegex.findFirstMatchIn }
      .map { coordinates =>
        val latitude  = coordinates.group(1).pipe { BigDecimal(_) }
        val longitude = coordinates.group(2).pipe { BigDecimal(_) }

        Coordinates(latitude = latitude, longitude = longitude)
      }
}
