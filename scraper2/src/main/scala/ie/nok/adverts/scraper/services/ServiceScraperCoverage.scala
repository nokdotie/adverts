package ie.nok.adverts.scraper.services

import ie.nok.adverts.Advert
import ie.nok.adverts.AdvertSaleStatus
import ie.nok.geographic.Coordinates
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.json.{DeriveJsonEncoder, JsonEncoder}
import zio.stream.ZStream

case class ServiceScraperCoverage(
    itemsCount: Int,
    itemsCountWithAdvertUrl: Int,
    itemsCountWithAdvertForSaleStatus: Int,
    itemsCountWithAdvertPriceInEur: Int,
    itemsCountWithPropertyDescription: Int,
    itemsCountWithPropertyType: Int,
    itemsCountWithPropertyAddress: Int,
    itemsCountWithPropertyEircode: Int,
    itemsCountWithPropertyCoordinates: Int,
    itemsCountWithPropertyImageUrls: Int,
    itemsCountWithPropertySize: Int,
    itemsCountWithPropertyBedroomsCount: Int,
    itemsCountWithPropertyBathroomsCount: Int,
    itemsCountWithPropertyBuildingEnergyRating: Int,
    itemsCountWithPropertyBuildingEnergyRatingCertificateNumber: Int,
    itemsCountWithPropertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Int,
    itemsCountWithFacets: Int,
    itemsCountWithAdvertiser: Int
)

object ServiceScraperCoverage {
  given JsonEncoder[ServiceScraperCoverage] = DeriveJsonEncoder.gen[ServiceScraperCoverage]

  val zero: ServiceScraperCoverage = ServiceScraperCoverage(
    itemsCount = 0,
    itemsCountWithAdvertUrl = 0,
    itemsCountWithAdvertForSaleStatus = 0,
    itemsCountWithAdvertPriceInEur = 0,
    itemsCountWithPropertyDescription = 0,
    itemsCountWithPropertyType = 0,
    itemsCountWithPropertyAddress = 0,
    itemsCountWithPropertyEircode = 0,
    itemsCountWithPropertyCoordinates = 0,
    itemsCountWithPropertyImageUrls = 0,
    itemsCountWithPropertySize = 0,
    itemsCountWithPropertyBedroomsCount = 0,
    itemsCountWithPropertyBathroomsCount = 0,
    itemsCountWithPropertyBuildingEnergyRating = 0,
    itemsCountWithPropertyBuildingEnergyRatingCertificateNumber = 0,
    itemsCountWithPropertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = 0,
    itemsCountWithFacets = 0,
    itemsCountWithAdvertiser = 0
  )

  def combine(a: ServiceScraperCoverage, b: ServiceScraperCoverage): ServiceScraperCoverage =
    ServiceScraperCoverage(
      itemsCount = a.itemsCount + b.itemsCount,
      itemsCountWithAdvertUrl = a.itemsCountWithAdvertUrl + b.itemsCountWithAdvertUrl,
      itemsCountWithAdvertForSaleStatus = a.itemsCountWithAdvertForSaleStatus + b.itemsCountWithAdvertForSaleStatus,
      itemsCountWithAdvertPriceInEur = a.itemsCountWithAdvertPriceInEur + b.itemsCountWithAdvertPriceInEur,
      itemsCountWithPropertyDescription = a.itemsCountWithPropertyDescription + b.itemsCountWithPropertyDescription,
      itemsCountWithPropertyType = a.itemsCountWithPropertyType + b.itemsCountWithPropertyType,
      itemsCountWithPropertyAddress = a.itemsCountWithPropertyAddress + b.itemsCountWithPropertyAddress,
      itemsCountWithPropertyEircode = a.itemsCountWithPropertyEircode + b.itemsCountWithPropertyEircode,
      itemsCountWithPropertyCoordinates = a.itemsCountWithPropertyCoordinates + b.itemsCountWithPropertyCoordinates,
      itemsCountWithPropertyImageUrls = a.itemsCountWithPropertyImageUrls + b.itemsCountWithPropertyImageUrls,
      itemsCountWithPropertySize = a.itemsCountWithPropertySize + b.itemsCountWithPropertySize,
      itemsCountWithPropertyBedroomsCount = a.itemsCountWithPropertyBedroomsCount + b.itemsCountWithPropertyBedroomsCount,
      itemsCountWithPropertyBathroomsCount = a.itemsCountWithPropertyBathroomsCount + b.itemsCountWithPropertyBathroomsCount,
      itemsCountWithPropertyBuildingEnergyRating = a.itemsCountWithPropertyBuildingEnergyRating + b.itemsCountWithPropertyBuildingEnergyRating,
      itemsCountWithPropertyBuildingEnergyRatingCertificateNumber =
        a.itemsCountWithPropertyBuildingEnergyRatingCertificateNumber + b.itemsCountWithPropertyBuildingEnergyRatingCertificateNumber,
      itemsCountWithPropertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
        a.itemsCountWithPropertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear + b.itemsCountWithPropertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      itemsCountWithFacets = a.itemsCountWithFacets + b.itemsCountWithFacets,
      itemsCountWithAdvertiser = a.itemsCountWithAdvertiser + b.itemsCountWithAdvertiser
    )

  private def countIfTrue(predicate: Boolean): Int           = if (predicate) 1 else 0
  private def countIfGreaterThanZero(value: Int): Int        = countIfTrue(value > 0)
  private def countIfGreaterThanZero(value: BigDecimal): Int = countIfTrue(value > 0)
  private def countIfNonEmpty(str: String): Int              = str.trim.nonEmpty.pipe(countIfTrue)
  private def countIfNonEmpty(opt: Option[String]): Int      = opt.getOrElse("").pipe(countIfNonEmpty)
  private def countIfNonEmpty(strs: List[String]): Int       = strs.mkString("").pipe(countIfNonEmpty)
  private def countIfDefined(opt: Option[_]): Int            = opt.isDefined.pipe(countIfTrue)

  def fromAdvert(advert: Advert): ServiceScraperCoverage =
    ServiceScraperCoverage(
      itemsCount = 1,
      itemsCountWithAdvertUrl = countIfNonEmpty(advert.advertUrl),
      itemsCountWithAdvertForSaleStatus = countIfTrue(advert.advertSaleStatus == AdvertSaleStatus.ForSale),
      itemsCountWithAdvertPriceInEur = countIfGreaterThanZero(advert.advertPriceInEur),
      itemsCountWithPropertyDescription = countIfNonEmpty(advert.propertyDescription),
      itemsCountWithPropertyType = countIfDefined(advert.propertyType),
      itemsCountWithPropertyAddress = countIfNonEmpty(advert.propertyAddress),
      itemsCountWithPropertyEircode = countIfDefined(advert.propertyEircode),
      itemsCountWithPropertyCoordinates = countIfTrue(advert.propertyCoordinates != Coordinates.zero),
      itemsCountWithPropertyImageUrls = countIfNonEmpty(advert.propertyImageUrls),
      itemsCountWithPropertySize = countIfGreaterThanZero(advert.propertySize.value),
      itemsCountWithPropertyBedroomsCount = countIfGreaterThanZero(advert.propertyBedroomsCount),
      itemsCountWithPropertyBathroomsCount = countIfGreaterThanZero(advert.propertyBathroomsCount),
      itemsCountWithPropertyBuildingEnergyRating = countIfDefined(advert.propertyBuildingEnergyRating),
      itemsCountWithPropertyBuildingEnergyRatingCertificateNumber = countIfDefined(advert.propertyBuildingEnergyRatingCertificateNumber),
      itemsCountWithPropertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
        countIfDefined(advert.propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear),
      itemsCountWithFacets = countIfNonEmpty(advert.facets.map(_.url)),
      itemsCountWithAdvertiser = countIfDefined(advert.advertiser)
    )

  def fromAdverts[R](stream: ZStream[R, Throwable, Advert]): ZIO[R, Throwable, ServiceScraperCoverage] =
    stream
      .map(fromAdvert)
      .runFold(zero)(combine)
}
