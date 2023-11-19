package ie.nok.adverts

import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.geographic.geojson.Feature
import ie.nok.unit.Area
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.Instant

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
    seller: Option[Seller],
    createdAt: Instant
)

object Advert {
  given JsonCodec[Advert] = DeriveJsonCodec.gen[Advert]
}
