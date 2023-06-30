package ie.nok.adverts.scraper.dngie

import ie.nok.adverts.Advert
import zio.http.Client
import zio.stream.ZStream

val advertStream: ZStream[Client, Throwable, Advert] = Properties.stream
