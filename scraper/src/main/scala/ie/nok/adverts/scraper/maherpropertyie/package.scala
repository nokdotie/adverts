package ie.nok.adverts.scraper.maherpropertyie

import ie.nok.adverts.Advert
import ie.nok.advertisers.stores.AdvertiserStore
import zio.http.Client
import zio.stream.ZStream

val advertStream: ZStream[Client & AdvertiserStore, Throwable, Advert] =
  Properties.stream
