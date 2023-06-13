package ie.nok.adverts.store

import ie.nok.adverts.Advert
import ie.nok.json.JsonDecoder
import ie.nok.env.Environment
import ie.nok.gcp.storage.{readAllBytes,Storage}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.{ZIO, ZLayer}

object AdvertsStoreImpl {
  val bucket: ZIO[Any, Throwable, String] =
    Environment.get
        .map {
        case Environment.Production => "nok-ie"
        case Environment.Other      => "nok-ie-dev"
        }

  val live: ZLayer[Storage, Throwable, AdvertsStore] = ZLayer.fromZIO {
    for {
        bucket <- bucket
        allBytes <- readAllBytes(bucket, "adverts/daft.ie/20230613010810.jsonl", List.empty)
        all <- allBytes
            .pipe { new String(_) }
            .linesIterator
            .map { JsonDecoder.decode[Advert] }
            .toList
            .pipe { ZIO.collectAll }
    } yield new AdvertsStoreImpl(all)
  }
}

class AdvertsStoreImpl(all: List[Advert]) extends AdvertsStore {

  def getPage(first: Int, after: AdvertsStoreCursor): ZIO[Any, Throwable, List[Advert]] =
    all.drop(after.index).take(first).pipe(ZIO.succeed)

}
