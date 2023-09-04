package ie.nok.adverts.aggregate

import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.adverts.stores.AdvertStoreImpl
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
            sources = adverts.flatMap { _.sources },
            createdAt = Instant.now
          )
      }

  def run =
    latest
      .map { merge }
      .map { Random.shuffle }
      .pipe { ZStream.fromIterableZIO }
      .pipe { AdvertStoreImpl.encodeAndWriteLatest }
      .provide(
        Storage.live,
        Scope.default
      )
}
