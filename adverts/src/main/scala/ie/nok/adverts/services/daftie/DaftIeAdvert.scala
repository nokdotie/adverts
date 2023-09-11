package ie.nok.adverts.services.daftie

import ie.nok.adverts.{Advert, InformationSource}
import ie.nok.hash.Hasher
import ie.nok.ber.Rating
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps
import zio.json.{JsonCodec, DeriveJsonCodec, EncoderOps}

case class DaftIeAdvert(
    url: String,
    priceInEur: Option[Int],
    address: String,
    coordinates: Coordinates,
    imageUrls: List[String],
    size: Option[Area],
    bedroomsCount: Option[Int],
    bathroomsCount: Option[Int],
    buildingEnergyRating: Option[Rating],
    buildingEnergyRatingCertificateNumber: Option[Int],
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Option[BigDecimal],
    createdAt: Instant
)

object DaftIeAdvert {
  def toAdvert(self: DaftIeAdvert): Advert =
    Advert(
      identifier = self.address.pipe { Hasher.hash },
      advertUrl = self.url,
      advertPriceInEur = self.priceInEur.getOrElse(0),
      propertyAddress = self.address,
      propertyCoordinates = self.coordinates,
      propertyImageUrls = self.imageUrls,
      propertySize = self.size.getOrElse(Area(0, AreaUnit.SquareMetres)),
      propertySizeInSqtMtr = self.size
        .map { Area.toSquareMetres }
        .fold(BigDecimal(0)) { _.value },
      propertyBedroomsCount = self.bedroomsCount.getOrElse(0),
      propertyBathroomsCount = self.bathroomsCount.getOrElse(0),
      propertyBuildingEnergyRating = self.buildingEnergyRating,
      propertyBuildingEnergyRatingCertificateNumber =
        self.buildingEnergyRatingCertificateNumber,
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
        self.buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      sources = List(InformationSource.DaftIeAdvert(self)),
      createdAt = self.createdAt
    )

  given JsonCodec[DaftIeAdvert] = DeriveJsonCodec.gen[DaftIeAdvert]
}
