package ie.nok.adverts.services.daftie

import ie.nok.adverts.{Advert, InformationSource}
import ie.nok.ber.Rating
import ie.nok.unit.{Area, AreaUnit}
import ie.nok.geographic.Coordinates
import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}

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
      sources = List(InformationSource.DaftIeAdvert(self)),
      createdAt = self.createdAt
    )

  given JsonCodec[DaftIeAdvert] = DeriveJsonCodec.gen[DaftIeAdvert]
}
