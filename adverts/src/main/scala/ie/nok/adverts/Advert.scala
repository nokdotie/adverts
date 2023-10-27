package ie.nok.adverts

import ie.nok.ber.Rating
import ie.nok.geographic.Coordinates
import ie.nok.geographic.geojson.Feature
import ie.nok.ecad.Eircode
import ie.nok.unit.Area
import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyIdentifier: String,
    propertyAddress: String,
    propertyEircode: Option[Eircode],
    propertyCoordinates: Coordinates,
    propertyImageUrls: List[String],
    propertySize: Area,
    propertySizeInSqtMtr: BigDecimal,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int,
    propertyBuildingEnergyRating: Option[Rating],
    propertyBuildingEnergyRatingCertificateNumber: Option[Int],
    propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Option[
      BigDecimal
    ],
    sources: List[InformationSource],
    createdAt: Instant
)

object Advert {
  given JsonCodec[Advert] = DeriveJsonCodec.gen[Advert]
}
