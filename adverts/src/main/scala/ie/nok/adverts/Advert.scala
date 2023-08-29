package ie.nok.adverts

import ie.nok.geographic.Coordinates
import ie.nok.geographic.geojson.Feature
import ie.nok.unit.Area
import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}
import ie.nok.adverts.Advert

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
    attributes: List[AdvertAttribute],
    createdAt: Instant
)

object Advert {
  given JsonCodec[Advert] = DeriveJsonCodec.gen[Advert]
}
