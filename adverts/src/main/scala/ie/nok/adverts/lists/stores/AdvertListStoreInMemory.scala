package ie.nok.adverts.lists.stores

import ie.nok.adverts.lists.AdvertList
import ie.nok.adverts.stores.AdvertFilter
import ie.nok.codecs.hash.Hash
import ie.nok.stores.filters.IntFilter
import java.time.{LocalDate, ZoneOffset}
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Random
import zio.{ZIO, ZLayer}

object AdvertListStoreInMemory {

  private def randomWithDayOfYearSeed: Random =
    LocalDate
      .now(ZoneOffset.UTC)
      .getDayOfYear
      .pipe { Random(_) }

  val live: ZLayer[Any, Nothing, AdvertListStore] =
    List(
      ("1 Beds", AdvertFilter.PropertyBedroomsCount(IntFilter.Equals(1)))
    )
      .map { (label, filter) => AdvertList(Hash.encode(label), label, filter) }
      .pipe { randomWithDayOfYearSeed.shuffle(_) }
      .pipe { new AdvertListStoreInMemory(_) }
      .pipe { ZLayer.succeed }

}

class AdvertListStoreInMemory(all: List[AdvertList]) extends AdvertListStore {
  def getPage(
      first: Int,
      after: AdvertListStoreCursor
  ): ZIO[Any, Throwable, List[AdvertList]] =
    all
      .slice(after.index, after.index + first)
      .pipe { ZIO.succeed }

}
