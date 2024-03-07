package ie.nok.adverts

import ie.nok.advertisers.Advertiser
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.Area
import java.time.Instant
import zio.json.{DeriveJsonCodec, JsonCodec}

case class Advert(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyIdentifier: String,
    propertyDescription: Option[String],
    propertyType: Option[PropertyType],
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
    advertiser: Option[Advertiser],
    createdAt: Instant
)

object Advert {
  given JsonCodec[Advert] = DeriveJsonCodec.gen[Advert]
}
