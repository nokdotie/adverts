package ie.nok.adverts.scraperV2

import ie.nok.adverts.scraperV2.daftie.Seller
import ie.nok.adverts.scraperV2.daftie.Seller.given_JsonCodec_Seller
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.Area
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.Instant

case class AdvertV2(
    advertUrl: String,
    advertPriceInEur: Int,
    propertyIdentifier: String,
    propertyAddress: String,
    propertyEircode: Option[Eircode],
    propertyCoordinates: Coordinates,
    propertyImageUrls: List[String],
    propertySize: Area,
    propertyBedroomsCount: Int,
    propertyBathroomsCount: Int,
    propertyBuildingEnergyRating: Option[Rating],
    propertyBuildingEnergyRatingCertificateNumber: Option[Int],
    propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Option[BigDecimal],
    seller: Seller,
    createdAt: Instant
)

object AdvertV2 {
  given JsonCodec[AdvertV2] = DeriveJsonCodec.gen[AdvertV2]
}
