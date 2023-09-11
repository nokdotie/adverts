package ie.nok.adverts.services.myhomeie

import ie.nok.adverts.{Advert, InformationSource}
import ie.nok.hash.Hasher
import ie.nok.ber.Rating
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps
import zio.json.{JsonCodec, DeriveJsonCodec, EncoderOps}

case class MyHomeIeAdvert(
    url: String,
    priceInEur: Option[Int],
    address: String,
    coordinates: Option[Coordinates],
    imageUrls: List[String],
    size: Option[Area],
    bedroomsCount: Option[Int],
    bathroomsCount: Option[Int],
    buildingEnergyRating: Option[Rating],
    createdAt: Instant
)

object MyHomeIeAdvert {
  def toAdvert(self: MyHomeIeAdvert): Advert =
    Advert(
      identifier = self.address.pipe { Hasher.hash },
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
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
      sources = List(InformationSource.MyHomeIeAdvert(self)),
      createdAt = self.createdAt
    )

  given JsonCodec[MyHomeIeAdvert] = DeriveJsonCodec.gen[MyHomeIeAdvert]
}
