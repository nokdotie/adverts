package ie.nok.adverts.scraper.services

import java.net.URL
import org.jsoup.nodes.Document
import ie.nok.adverts.{Advert, AdvertSaleStatus, PropertyType}
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.ber.Rating
import ie.nok.unit.Area
import ie.nok.codecs.hash.Hash
import java.time.Instant

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
      advertUrl = document.baseUri(),
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
      advertiser = None,
      createdAt = Instant.now()
    )
  }

}
