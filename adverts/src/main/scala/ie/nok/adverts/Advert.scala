package ie.nok.adverts

import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyAddress: String,
    propertyImageUrls: List[String],
    propertySizeInSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int,
    createdAt: Instant
)

object Advert {
  given JsonCodec[Advert] = DeriveJsonCodec.gen[Advert]
}
