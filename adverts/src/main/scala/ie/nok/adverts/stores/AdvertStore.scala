package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.stores.pagination.Page
import zio.ZIO

trait AdvertStore {
  def getPage(
      filter: AdvertFilter,
      sort: AdvertSort,
      first: Int,
      after: AdvertCursor
  ): ZIO[Any, Throwable, Page[(Advert, AdvertCursor)]]
}

object AdvertStore {
  def getPage(
      filter: AdvertFilter,
      sort: AdvertSort,
      first: Int,
      after: AdvertCursor
  ): ZIO[AdvertStore, Throwable, Page[(Advert, AdvertCursor)]] =
    ZIO.serviceWithZIO[AdvertStore](_.getPage(filter, sort, first, after))
}
