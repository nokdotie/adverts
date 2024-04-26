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

trait ServiceItemPageScraper {

  def filter(document: Document): Boolean

  def getSaleStatus(document: Document): AdvertSaleStatus
  def getPriceInEur(document: Document): Int
  def getDescription(document: Document): Option[String]
  def getPropertyType(document: Document): Option[PropertyType]
  def getAddress(document: Document): String
  def getEircode(document: Document): Option[Eircode]
  def getCoordinates(document: Document): Coordinates
  def getImageUrls(document: Document): List[String]
  def getSize(document: Document): Area
  def getBedroomsCount(document: Document): Int
  def getBathroomsCount(document: Document): Int
  def getBuildingEnergyRating(document: Document): Option[Rating]
  def getBuildingEnergyRatingCertificateNumber(document: Document): Option[Int]
  def getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(document: Document): Option[BigDecimal]
  def getPropertyServicesProviderLicenceNumber(document: Document): Option[String]

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
