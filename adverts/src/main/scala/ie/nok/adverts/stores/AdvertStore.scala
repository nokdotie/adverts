package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import java.time.Instant
import zio.{ZIO, ZLayer}

case class AdvertStoreCursor(index: Int)
trait AdvertStore {
  def get(identifier: String): ZIO[Any, Throwable, Option[Advert]]

  def getPage(
      filter: AdvertFilter,
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[Any, Throwable, List[Advert]]
}

object AdvertStore {
  def get(identifier: String): ZIO[AdvertStore, Throwable, Option[Advert]] =
    ZIO.serviceWithZIO[AdvertStore](_.get(identifier))

  def getPage(
      filter: AdvertFilter,
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[AdvertStore, Throwable, List[Advert]] =
    ZIO.serviceWithZIO[AdvertStore](_.getPage(filter, first, after))
}
