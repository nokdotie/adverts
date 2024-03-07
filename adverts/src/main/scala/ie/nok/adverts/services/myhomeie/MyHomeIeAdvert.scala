package ie.nok.adverts.services.myhomeie

import ie.nok.advertisers.Advertiser
import ie.nok.adverts.{Advert, InformationSource, PropertyType}
import ie.nok.ber.Rating
import ie.nok.geographic.Coordinates
import ie.nok.codecs.hash.Hash
import ie.nok.unit.{Area, AreaUnit}
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps

case class MyHomeIeAdvert(
    url: String,
    priceInEur: Option[Int],
    description: Option[String],
    address: String,
    propertyType: Option[PropertyType],
    coordinates: Option[Coordinates],
    imageUrls: List[String],
    size: Option[Area],
    bedroomsCount: Option[Int],
    bathroomsCount: Option[Int],
    buildingEnergyRating: Option[Rating],
    advertiser: Option[Advertiser],
    createdAt: Instant
)

object MyHomeIeAdvert {
  def toAdvert(self: MyHomeIeAdvert): Advert =
    Advert(
      advertUrl = self.url,
      advertPriceInEur = self.priceInEur.getOrElse(0),
      propertyIdentifier = self.address.pipe { Hash.encode },
      propertyDescription = self.description,
      propertyAddress = self.address,
      propertyType = self.propertyType,
      propertyEircode = None,
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
      advertiser = self.advertiser,
      createdAt = self.createdAt
    )

  given JsonCodec[MyHomeIeAdvert] = DeriveJsonCodec.gen[MyHomeIeAdvert]
}
