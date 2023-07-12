package ie.nok.adverts

import ie.nok.unit.{Area, Coordinates}
import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyAddress: String,
    propertyCoordinates: Coordinates,
    propertyImageUrls: List[String],
    propertySize: Area,
    propertySizeInSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int,
    createdAt: Instant
)

object Advert {
  given JsonCodec[Advert] = DeriveJsonCodec.gen[Advert]
}
