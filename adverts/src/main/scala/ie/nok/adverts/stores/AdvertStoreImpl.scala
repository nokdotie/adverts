package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.json.JsonDecoder
import ie.nok.env.Environment
import ie.nok.gcp.storage.{readAllBytes, Storage}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.{ZIO, ZLayer}

object AdvertStoreImpl {
  val bucket: ZIO[Any, Throwable, String] =
    Environment.get
      .map {
        case Environment.Production => "nok-ie"
        case Environment.Other      => "nok-ie-dev"
      }

  val live: ZLayer[Storage, Throwable, AdvertStore] = ZLayer.fromZIO {
    for {
      bucket <- bucket
      allBytes <- readAllBytes(
        bucket,
        "adverts/daft.ie/20230613010810.jsonl",
        List.empty
      )
      all <- allBytes
        .pipe { new String(_) }
        .linesIterator
        .map { JsonDecoder.decode[Advert] }
        .toList
        .pipe { ZIO.collectAll }
    } yield new AdvertStoreImpl(all)
  }
}

class AdvertStoreImpl(all: List[Advert]) extends AdvertStore {

  def getPage(
      filter: AdvertFilter,
      first: Int,
      after: AdvertStoreCursor
  ): ZIO[Any, Throwable, List[Advert]] =
    all
      .filter(filter.filter)
      .drop(after.index)
      .take(first)
      .pipe(ZIO.succeed)

}
