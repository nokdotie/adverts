package ie.deed

import zio.http.Client
import zio.stream.ZStream

trait Scraper:
  val scrape: ZStream[Client, Throwable, Record]
