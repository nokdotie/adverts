package ie.deed

import java.time.Instant
import zio.json._

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

object Record {
  implicit val encoder: JsonEncoder[Record] = DeriveJsonEncoder.gen[Record]
}
