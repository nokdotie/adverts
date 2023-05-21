package ie.deed.adverts

import java.time.Instant
import zio.json._

case class Record(
    at: Instant,
    advertUrl: String,
    advertPrice: Int,
    propertyEircode: String,
    propertyImageUrls: List[String],
    contactName: String,
    contactPhone: Option[String],
    contactEmail: Option[String]
)

object Record {
  given JsonEncoder[Record] = DeriveJsonEncoder.gen[Record]
}
