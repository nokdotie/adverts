package ie.nok.adverts

import java.time.Instant
import zio.json._

case class Advert(
    at: Instant,
    advertUrl: String,
    advertPrice: Int,
    propertyEircode: String,
    propertyImageUrls: List[String],
    contactName: String,
    contactPhone: Option[String],
    contactEmail: Option[String]
)

object Advert {
  given JsonEncoder[Advert] = DeriveJsonEncoder.gen[Advert]
}
