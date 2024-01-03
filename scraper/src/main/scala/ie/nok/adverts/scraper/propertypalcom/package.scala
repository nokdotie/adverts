package ie.nok.adverts.scraper.propertypalcom

import ie.nok.advertisers.stores.AdvertiserStore
import ie.nok.adverts.Advert
import scala.util.chaining._
import zio.http.Client
import zio.stream.ZStream

val advertStream: ZStream[Client & AdvertiserStore, Throwable, Advert] =
  BuildId.latest
    .pipe { ZStream.fromZIO }
    .flatMap { buildId =>
      Properties
        .stream(buildId)
        .via(Property.pipeline(buildId))
    }
