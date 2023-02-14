package ie.deed

import java.time.Instant
import zio.http.Client
import zio.stream.ZStream

trait Scraper:
  val scrape: ZStream[Client, Throwable, Record]

case class Record(
    at: Instant,
    advertUrl: String,
    advertPrice: Int,
    propertyEircode: String,
    propertyImageUrls: List[String],
    contactName: String,
    contactPhone: String,
    contactEmail: String
)
