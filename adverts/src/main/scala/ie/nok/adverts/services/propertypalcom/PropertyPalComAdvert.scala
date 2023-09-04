package ie.nok.adverts.services.propertypalcom

import ie.nok.adverts.{Advert, InformationSource}
import ie.nok.ber.Rating
import ie.nok.unit.{Area, AreaUnit}
import ie.nok.geographic.Coordinates
import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}

case class PropertyPalComAdvert(
    url: String,
    priceInEur: Option[Int],
    address: String,
    coordinates: Option[Coordinates],
    imageUrls: List[String],
    size: Option[Area],
    bedroomsCount: Option[Int],
    bathroomsCount: Option[Int],
    buildingEnergyRating: Option[Rating],
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Option[BigDecimal],
    createdAt: Instant
)

object PropertyPalComAdvert {
  def toAdvert(self: PropertyPalComAdvert): Advert =
    Advert(
      advertUrl = self.url,
      advertPriceInEur = self.priceInEur.getOrElse(0),
      propertyAddress = self.address,
      propertyCoordinates = self.coordinates.getOrElse(Coordinates.zero),
      propertyImageUrls = self.imageUrls,
      propertySize = self.size.getOrElse(Area(0, AreaUnit.SquareMetres)),
      propertySizeInSqtMtr = self.size
        .map { Area.toSquareMetres }
        .fold(BigDecimal(0)) { _.value },
      propertyBedroomsCount = self.bedroomsCount.getOrElse(0),
      propertyBathroomsCount = self.bathroomsCount.getOrElse(0),
      propertyBuildingEnergyRating = self.buildingEnergyRating,
      propertyBuildingEnergyRatingCertificateNumber = None,
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
        self.buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      sources = List(InformationSource.PropertyPalComAdvert(self)),
      createdAt = self.createdAt
    )

  given JsonCodec[PropertyPalComAdvert] =
    DeriveJsonCodec.gen[PropertyPalComAdvert]
}
