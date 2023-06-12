package ie.nok.adverts

import java.time.Instant
import zio.json._

case class Advert(
    advertUrl: String,
    advertPrice: Int,
    propertyAddress: String,
    propertyImageUrls: List[String],
    propertySizeinSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int,
    createdAt: Instant
)

object Advert {
  given JsonEncoder[Advert] = DeriveJsonEncoder.gen[Advert]
}
