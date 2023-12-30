package ie.nok.adverts.scraper.myhomeie

import ie.nok.advertisers.stores.AdvertiserStore
import ie.nok.adverts.Advert
import scala.util.chaining.scalaUtilChainingOps
import zio.http.Client
import zio.stream.ZStream

val advertStream: ZStream[Client & AdvertiserStore, Throwable, Advert] =
  ApiKey.latest
    .pipe { ZStream.fromZIO }
    .flatMap { apiKey =>
      Properties.stream.via(Property.pipeline(apiKey))
    }
