package ie.nok.adverts.services.propertypalcom

import ie.nok.advertisers.Advertiser
import ie.nok.adverts.{Advert, AdvertSaleStatus, InformationSource, PropertyType}
import ie.nok.codecs.hash.Hash
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}

import java.time.Instant
import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps
import zio.json.{DeriveJsonCodec, EncoderOps, JsonCodec}

case class PropertyPalComAdvert(
    url: String,
    saleStatus: AdvertSaleStatus,
    priceInEur: Option[Int],
    address: String,
    description: Option[String],
    propertyType: Option[PropertyType],
    eircode: Option[Eircode],
    coordinates: Option[Coordinates],
    imageUrls: List[String],
    size: Option[Area],
    bedroomsCount: Option[Int],
    bathroomsCount: Option[Int],
    buildingEnergyRating: Option[Rating],
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear: Option[BigDecimal],
    advertiser: Option[Advertiser],
    createdAt: Instant
)

object PropertyPalComAdvert {
  def toAdvert(self: PropertyPalComAdvert): Advert =
    Advert(
      advertUrl = self.url,
      advertSaleStatus = self.saleStatus,
      advertPriceInEur = self.priceInEur.getOrElse(0),
      propertyIdentifier = self.address.pipe { Hash.encode },
      propertyDescription = self.description,
      propertyType = self.propertyType,
      propertyAddress = self.address,
      propertyEircode = self.eircode,
      propertyCoordinates = self.coordinates.getOrElse(Coordinates.zero),
      propertyImageUrls = self.imageUrls,
      propertySize = self.size.getOrElse(Area.zero),
      propertySizeInSqtMtr = self.size
        .map { Area.toSquareMetres }
        .fold(BigDecimal(0)) { _.value },
      propertyBedroomsCount = self.bedroomsCount.getOrElse(0),
      propertyBathroomsCount = self.bathroomsCount.getOrElse(0),
      propertyBuildingEnergyRating = self.buildingEnergyRating,
      propertyBuildingEnergyRatingCertificateNumber = None,
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = self.buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      sources = List(InformationSource.PropertyPalComAdvert(self)),
      advertiser = self.advertiser,
      createdAt = self.createdAt
    )

  given JsonCodec[PropertyPalComAdvert] =
    DeriveJsonCodec.gen[PropertyPalComAdvert]
}
