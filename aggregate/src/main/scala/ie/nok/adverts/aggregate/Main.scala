package ie.nok.adverts.aggregate

import ie.nok.adverts.ber.{ZCertificateStore, ZCertificateStoreImpl}
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.adverts.{Advert, AdvertFacet, AdvertSaleStatus, AdvertService}
import ie.nok.ber.Certificate
import ie.nok.ber.stores.GoogleFirestoreCertificateStore
import ie.nok.geographic.Coordinates
import ie.nok.google.firestore.Firestore
import ie.nok.stores.compose.{ZFileAndGoogleStorageStore, ZFileAndGoogleStorageStoreImpl}
import ie.nok.unit.Area
import zio.stream.ZStream
import zio.{Scope, ZIO, ZIOAppDefault}

import java.time.Instant
import scala.util.Random
import scala.util.chaining.scalaUtilChainingOps

object Main extends ZIOAppDefault {
  private val latest: ZIO[ZFileAndGoogleStorageStore[Advert], Throwable, List[Advert]] =
    AdvertService.values.toList
      .map { AdvertStoreImpl.readAndDecodeLatestForService }
      .pipe { ZIO.collectAll }
      .map { _.flatten }

  private def groupByProperty(adverts: List[Advert]): List[List[Advert]] =
    adverts
      .groupBy { _.propertyAddress }
      .values
      .toList

  private def forSale(adverts: List[Advert]): Boolean =
    adverts.forall { _.advertSaleStatus == AdvertSaleStatus.ForSale }

  private def mergeAdverts(adverts: List[Advert]): Advert =
    adverts
      .pipe {
        case Nil           => ???
        case advert :: Nil => advert
        case adverts =>
          val size = adverts
            .map { _.propertySize }
            .maxBy { size => Area.toSquareMetres(size).value }

          Advert(
            advertUrl = adverts.head.advertUrl,
            advertSaleStatus = AdvertSaleStatus.ForSale,
            advertPriceInEur = adverts.map { _.advertPriceInEur }.max,
            propertyIdentifier = adverts.head.propertyIdentifier,
            propertyDescription = adverts.flatMap { _.propertyDescription }.headOption,
            propertyType = adverts.flatMap { _.propertyType }.headOption,
            propertyAddress = adverts.head.propertyAddress,
            propertyEircode = adverts.flatMap { _.propertyEircode }.headOption,
            propertyCoordinates = adverts
              .map { _.propertyCoordinates }
              .find { _ != Coordinates.zero }
              .getOrElse(Coordinates.zero),
            propertyImageUrls = adverts.map { _.propertyImageUrls }.maxBy { _.length },
            propertySize = size,
            propertySizeInSqtMtr = Area.toSquareMetres(size).value,
            propertyBedroomsCount = adverts.map { _.propertyBedroomsCount }.max,
            propertyBathroomsCount = adverts.map { _.propertyBathroomsCount }.max,
            propertyBuildingEnergyRating = adverts.flatMap { _.propertyBuildingEnergyRating }.headOption,
            propertyBuildingEnergyRatingCertificateNumber = adverts.flatMap {
              _.propertyBuildingEnergyRatingCertificateNumber
            }.headOption,
            propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = adverts.flatMap {
              _.propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear
            }.headOption,
            facets = adverts.flatMap { _.facets }.distinct.sortBy { _.url },
            advertiser = adverts
              .flatMap(_.advertiser)
              .headOption,
            createdAt = Instant.now
          )
      }

  def appendBuildingEnergyRating(advert: Advert, certificates: List[Certificate]): Advert = {
    val latestCertificate = certificates.maxByOption { _.issuedOn }

    advert.copy(
      propertyBuildingEnergyRating = latestCertificate.map(_.rating).orElse(advert.propertyBuildingEnergyRating),
      propertyBuildingEnergyRatingCertificateNumber = latestCertificate.map(_.number.value).orElse(advert.propertyBuildingEnergyRatingCertificateNumber),
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
        latestCertificate.map(_.energyRating.value).map(BigDecimal(_)).orElse(advert.propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear),
      facets = (advert.facets ++ certificates.map { c => AdvertFacet(c.url) }).sortBy(_.url)
    )
  }

  def run = latest
    .map { groupByProperty }
    .map { Random.shuffle }
    .pipe { ZStream.fromIterableZIO }
    .filter { forSale }
    .mapZIOParUnordered(5) { adverts =>
      ZCertificateStore
        .getAll(adverts)
        .map { (adverts, _) }
    }
    .map { (adverts, certificates) => (mergeAdverts(adverts), certificates) }
    .map { (advert, certificates) => appendBuildingEnergyRating(advert, certificates) }
    .pipe { AdvertStoreImpl.encodeAndWriteLatest }
    .provide(
      Firestore.live,
      GoogleFirestoreCertificateStore.layer,
      Scope.default,
      ZFileAndGoogleStorageStoreImpl.layer[Advert],
      ZCertificateStoreImpl.layer
    )
}
