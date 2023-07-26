package ie.nok.adverts

import ie.nok.geographic.Coordinates
import ie.nok.geographic.geojson.Feature
import ie.nok.unit.Area
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

  def toGeoJsonFeature(advert: Advert): Feature[Advert] =
    Feature(
      geometry = Coordinates.toGeoJsonGeometry(advert.propertyCoordinates),
      properties = advert
    )
}
