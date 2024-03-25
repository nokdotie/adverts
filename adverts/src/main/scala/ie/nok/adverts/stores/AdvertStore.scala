package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.stores.pagination.Page
import java.time.Instant
import zio.{ZIO, ZLayer}

case class AdvertStoreCursor(index: Int)
trait AdvertStore {
  def getPage(
      filter: AdvertFilter,
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[Any, Throwable, Page[Advert]]
}

object AdvertStore {
  def getPage(
      filter: AdvertFilter,
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[AdvertStore, Throwable, Page[Advert]] =
    ZIO.serviceWithZIO[AdvertStore](_.getPage(filter, first, after))
}
