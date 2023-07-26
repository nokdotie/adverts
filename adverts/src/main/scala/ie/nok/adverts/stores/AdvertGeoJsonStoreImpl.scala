package ie.nok.adverts.stores

import com.google.cloud.storage.BlobInfo
import ie.nok.adverts.{Advert, AdvertService}
import ie.nok.json.JsonDecoder
import ie.nok.env.Environment
import ie.nok.gcp.storage.{createFrom, readAllBytes, Storage}
import java.nio.charset.StandardCharsets.UTF_8
import java.time.{Instant, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, Chunk, Scope, ZIO, ZLayer}
import zio.json._
import zio.nio.file.Files
import zio.stream.ZStream
import ie.nok.adverts.stores.AdvertStoreImpl
import ie.nok.geographic.geojson.{Feature, FeatureCollection}

object AdvertGeoJsonStoreImpl {
  private val blobNameLatest: String = "adverts/latest.geojson"

  protected[adverts] def encodeAndWriteLatest[R](
      stream: ZStream[R, Throwable, Advert]
  ): ZIO[R & Scope & Storage, Throwable, Unit] = for {
    geojson <- stream
      .map { Advert.toGeoJsonFeature }
      .runFold(List.empty[Feature[Advert]]) { _ :+ _ }
      .map { features => FeatureCollection(features = features) }
      .map { _.toJson }

    path <- Files.createTempFileScoped()
    _ <- geojson
      .getBytes(UTF_8)
      .pipe { Chunk.fromArray }
      .pipe { Files.writeBytes(path, _) }

    bucket <- AdvertStoreImpl.bucket
    blobInfo = BlobInfo.newBuilder(bucket, blobNameLatest).build
    _ <- createFrom(blobInfo, path.toFile.toPath, List.empty)
  } yield ()

}
