package ie.nok.adverts.store

import ie.nok.adverts.Advert
import java.time.Instant
import zio.{ZIO, ZLayer}

case class AdvertsStoreCursor(index: Int)
trait AdvertsStore {
  def getPage(first: Int, after: AdvertsStoreCursor): ZIO[Any, Throwable, List[Advert]]
}

object AdvertsStore {
  def getPage(first: Int, after: AdvertsStoreCursor): ZIO[AdvertsStore, Throwable, List[Advert]] =
    ZIO.serviceWithZIO[AdvertsStore](_.getPage(first, after))
}
