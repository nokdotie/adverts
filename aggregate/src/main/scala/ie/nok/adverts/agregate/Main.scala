package ie.nok.adverts.aggregate

import ie.nok.adverts.{Advert, AdvertService, InformationSource}
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.ber.CertificateNumber
import ie.nok.ber.stores.{CertificateStore, GoogleFirestoreCertificateStore}
import ie.nok.gcp.firestore.Firestore
import ie.nok.gcp.storage.Storage
import ie.nok.unit.Area
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Random
import zio.{Scope, ZIO, ZIOAppDefault}
import zio.stream.ZStream

object Main extends ZIOAppDefault {
  val latest: ZIO[Storage, Throwable, List[Advert]] =
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
          Advert(
            advertUrl = adverts.head.advertUrl,
            advertPriceInEur = adverts.map { _.advertPriceInEur }.max,
            propertyAddress = adverts.head.propertyAddress,
            propertyCoordinates = adverts.head.propertyCoordinates,
            propertyImageUrls = adverts.head.propertyImageUrls,
            propertySize = adverts.map { _.propertySize }.maxBy { _.value },
            propertySizeInSqtMtr = adverts
              .map { _.propertySize }
              .maxBy { _.value }
              .pipe { Area.toSquareMetres }
              .value,
            propertyBedroomsCount = adverts.map { _.propertyBedroomsCount }.max,
            propertyBathroomsCount =
              adverts.map { _.propertyBathroomsCount }.max,
            propertyBuildingEnergyRating =
              adverts.head.propertyBuildingEnergyRating,
            propertyBuildingEnergyRatingCertificateNumber =
              adverts.head.propertyBuildingEnergyRatingCertificateNumber,
            propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
              adverts.head.propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
            sources = adverts.flatMap { _.sources },
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
        case InformationSource.MyHomeIeAdvert(_) |
            InformationSource.PropertyPalComAdvert(_) |
            InformationSource.BuildingEnergyRatingCertificate(_) =>
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
          propertyBuildingEnergyRatingCertificateNumber =
            certificates.headOption.map(_.number.value),
          propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
            certificates.headOption
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
