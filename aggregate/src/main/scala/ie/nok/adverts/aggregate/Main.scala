package ie.nok.adverts.aggregate

import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.adverts.{Advert, AdvertService, InformationSource}
import ie.nok.ber.CertificateNumber
import ie.nok.ber.stores.{CertificateStore, GoogleFirestoreCertificateStore}
import ie.nok.gcp.firestore.Firestore
import ie.nok.gcp.storage.Storage
import ie.nok.geographic.Coordinates
import ie.nok.unit.Area
import zio.stream.ZStream
import zio.{Scope, ZIO, ZIOAppDefault}

import java.time.Instant
import scala.util.Random
import scala.util.chaining.scalaUtilChainingOps

object Main extends ZIOAppDefault {
  private val latest: ZIO[Storage, Throwable, List[Advert]] =
    AdvertService.values.toList
      .map { AdvertStoreImpl.readAndDecodeLatestForService }
      .pipe { ZIO.collectAll }
      .map { _.flatten }

  def merge(adverts: List[Advert]): Iterable[Advert] =
    adverts
      .groupBy { _.propertyAddress }
      .values
      .map {
        case Nil           => ???
        case advert :: Nil => advert
        case adverts =>
          val size = adverts
            .map { _.propertySize }
            .maxBy { size => Area.toSquareMetres(size).value }

          Advert(
            advertUrl = adverts.head.advertUrl,
            advertPriceInEur = adverts.map { _.advertPriceInEur }.max,
            propertyIdentifier = adverts.head.propertyIdentifier,
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
            sources = adverts.flatMap { _.sources }.distinct,
            advertiser = adverts
              .flatMap(_.advertiser)
              .headOption,
            createdAt = Instant.now
          )
      }

  def appendBuildingEnergyRating(
      adverts: Advert
  ): ZIO[CertificateStore, Throwable, Advert] =
    adverts.sources
      .flatMap {
        case InformationSource.DaftIeAdvert(daftIeAdvert) =>
          daftIeAdvert.buildingEnergyRatingCertificateNumber
        case InformationSource.DngIeAdvert(dngIeAdvert) =>
          dngIeAdvert.buildingEnergyRatingCertificateNumber
        case InformationSource.SherryFitzIeAdvert(sherryFitzIeAdvert) =>
          sherryFitzIeAdvert.buildingEnergyRatingCertificateNumber
        case InformationSource.MyHomeIeAdvert(_) | InformationSource.PropertyPalComAdvert(_) | InformationSource.BuildingEnergyRatingCertificate(_) =>
          None
      }
      .distinct
      .map { CertificateNumber.apply }
      .map { CertificateStore.getByNumber }
      .pipe { ZIO.collectAll }
      .map { _.flatten }
      .map { certificates =>
        val certificatesAsSources = certificates
          .map { InformationSource.BuildingEnergyRatingCertificate.apply }

        adverts.copy(
          propertyBuildingEnergyRating = certificates.headOption.map(_.rating),
          propertyBuildingEnergyRatingCertificateNumber = certificates.headOption.map(_.number.value),
          propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = certificates.headOption
            .map(_.energyRating.value)
            .map(BigDecimal(_)),
          sources = adverts.sources ++ certificatesAsSources
        )
      }

  def run =
    latest
      .map { merge }
      .map { Random.shuffle }
      .pipe { ZStream.fromIterableZIO }
      .mapZIOParUnordered(5) { appendBuildingEnergyRating }
      .pipe { AdvertStoreImpl.encodeAndWriteLatest }
      .provide(
        Firestore.live,
        GoogleFirestoreCertificateStore.layer,
        Storage.live,
        Scope.default
      )
}
