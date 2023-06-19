package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import java.time.Instant
import zio.{ZIO, ZLayer}

case class AdvertStoreCursor(index: Int)
trait AdvertStore {
  def getPage(
      filter: AdvertFilter,
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[Any, Throwable, List[Advert]]
}

object AdvertStore {
  def getPage(
      filter: AdvertFilter,
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[AdvertStore, Throwable, List[Advert]] =
    ZIO.serviceWithZIO[AdvertStore](_.getPage(filter, first, after))
}
