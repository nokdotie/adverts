package ie.nok.adverts.aggregate

import ie.nok.adverts.Advert
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.gcp.storage.Storage
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Random
import zio.{Scope, ZIO, ZIOAppDefault}
import zio.stream.ZStream

object Main extends ZIOAppDefault {
  val latest: ZIO[Storage, Throwable, List[Advert]] =
    List("daft.ie", "dng.ie", "myhome.ie", "propertypal.com", "sherryfitz.ie")
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
            propertyImageUrls = adverts.head.propertyImageUrls,
            propertySizeInSqtMtr = adverts.map { _.propertySizeInSqtMtr }.max,
            propertyBedroomsCount = adverts.map { _.propertyBedroomsCount }.max,
            propertyBathroomsCount =
              adverts.map { _.propertyBathroomsCount }.max,
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
