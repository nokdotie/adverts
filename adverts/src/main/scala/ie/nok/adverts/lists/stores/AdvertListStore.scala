package ie.nok.adverts.lists.stores

import ie.nok.adverts.lists.AdvertList
import java.time.Instant
import zio.{ZIO, ZLayer}

case class AdvertListStoreCursor(index: Int)
trait AdvertListStore {
  def getPage(
      first: Int,
      after: AdvertListStoreCursor
  ): ZIO[Any, Throwable, List[AdvertList]]
}

object AdvertListStore {
  def getPage(
      first: Int,
      after: AdvertListStoreCursor
  ): ZIO[AdvertListStore, Throwable, List[AdvertList]] =
    ZIO.serviceWithZIO[AdvertListStore](_.getPage(first, after))
}
