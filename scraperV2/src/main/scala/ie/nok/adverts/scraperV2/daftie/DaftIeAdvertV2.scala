package ie.nok.adverts.scraperV2.daftie

import ie.nok.adverts.scraperV2.AdvertV2
import ie.nok.adverts.scraperV2.daftie.Seller.given_JsonCodec_Seller
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.hash.Hasher
import ie.nok.unit.{Area, AreaUnit}
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps

case class DaftIeAdvertV2(
    url: String,
    priceInEur: Option[Int],
    address: String,
    eircode: Option[Eircode],
    coordinates: Coordinates,
    imageUrls: List[String],
    size: Option[Area],
    bedroomsCount: Option[Int],
    bathroomsCount: Option[Int],
    buildingEnergyRating: Option[Rating],
    buildingEnergyRatingCertificateNumber: Option[Int],
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Option[BigDecimal],
    seller: Seller,
    createdAt: Instant
)

object DaftIeAdvertV2 {
  def toAdvert(self: DaftIeAdvertV2): AdvertV2 =
    AdvertV2(
      advertUrl = self.url,
      advertPriceInEur = self.priceInEur.getOrElse(0),
      propertyIdentifier = self.address.pipe { Hasher.hash },
      propertyAddress = self.address,
      propertyEircode = self.eircode,
      propertyCoordinates = self.coordinates,
      propertyImageUrls = self.imageUrls,
      propertySize = self.size.getOrElse(Area(0, AreaUnit.SquareMetres)),
      propertySizeInSqtMtr = self.size.map { Area.toSquareMetres }.fold(BigDecimal(0)) { _.value },
      propertyBedroomsCount = self.bedroomsCount.getOrElse(0),
      propertyBathroomsCount = self.bathroomsCount.getOrElse(0),
      propertyBuildingEnergyRating = self.buildingEnergyRating,
      propertyBuildingEnergyRatingCertificateNumber = self.buildingEnergyRatingCertificateNumber,
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = self.buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      seller = self.seller,
      createdAt = self.createdAt
    )

  given JsonCodec[DaftIeAdvertV2] = DeriveJsonCodec.gen[DaftIeAdvertV2]
}
