package ie.nok.adverts.services.dngie

import ie.nok.adverts.{Advert, AdvertSaleStatus, PropertyType}
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.codecs.hash.Hash
import ie.nok.unit.{Area, AreaUnit}
import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import ie.nok.adverts.AdvertFacet

case class DngIeAdvert(
    url: String,
    saleStatus: AdvertSaleStatus,
    priceInEur: Option[Int],
    description: String,
    propertyType: Option[PropertyType],
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
    createdAt: Instant
)

object DngIeAdvert {
  def toAdvert(self: DngIeAdvert): Advert =
    Advert(
      advertUrl = self.url,
      advertSaleStatus = self.saleStatus,
      advertPriceInEur = self.priceInEur.getOrElse(0),
      propertyIdentifier = self.address.pipe { Hash.encode },
      propertyDescription = Option(self.description),
      propertyType = self.propertyType,
      propertyAddress = self.address,
      propertyEircode = self.eircode,
      propertyCoordinates = self.coordinates,
      propertyImageUrls = self.imageUrls,
      propertySize = self.size.getOrElse(Area.zero),
      propertySizeInSqtMtr = self.size
        .map { Area.toSquareMetres }
        .fold(BigDecimal(0)) { _.value },
      propertyBedroomsCount = self.bedroomsCount.getOrElse(0),
      propertyBathroomsCount = self.bathroomsCount.getOrElse(0),
      propertyBuildingEnergyRating = self.buildingEnergyRating,
      propertyBuildingEnergyRatingCertificateNumber = self.buildingEnergyRatingCertificateNumber,
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = self.buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
      facets = List(AdvertFacet(self.url)),
      advertiser = None,
      createdAt = self.createdAt
    )

  given JsonCodec[DngIeAdvert] = DeriveJsonCodec.gen[DngIeAdvert]
}
