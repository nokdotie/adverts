package ie.nok.adverts.services.maherpropertyie

import ie.nok.advertisers.stores.AdvertiserStoreInMemory
import ie.nok.adverts.{Advert, AdvertSaleStatus, InformationSource, PropertyType}
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.codecs.hash.Hash
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.json.{DeriveJsonCodec, JsonCodec}
import ie.nok.adverts.AdvertFacet

case class MaherPropertyIeAdvert(
    url: String,
    saleStatus: AdvertSaleStatus,
    priceInEur: Option[Int],
    description: String,
    address: String,
    eircode: Option[Eircode],
    imageUrls: List[String],
    sizeInSqtMtr: Option[Int],
    bedroomsCount: Option[Int],
    bathroomsCount: Option[Int],
    createdAt: Instant
)

object MaherPropertyIeAdvert {
  def toAdvert(self: MaherPropertyIeAdvert): Advert =
    Advert(
      advertUrl = self.url,
      advertSaleStatus = AdvertSaleStatus.ForSale,
      advertPriceInEur = self.priceInEur.getOrElse(0),
      propertyIdentifier = self.address.pipe { Hash.encode },
      propertyDescription = Some(self.description),
      propertyAddress = self.address,
      propertyType = None,
      propertyEircode = self.eircode,
      propertyCoordinates = Coordinates.zero,
      propertyImageUrls = self.imageUrls,
      propertySize = self.sizeInSqtMtr.fold(Area.zero)(Area(_, AreaUnit.SquareMetres)),
      propertySizeInSqtMtr = self.sizeInSqtMtr.getOrElse(0),
      propertyBedroomsCount = self.bedroomsCount.getOrElse(0),
      propertyBathroomsCount = self.bathroomsCount.getOrElse(0),
      propertyBuildingEnergyRating = None,
      propertyBuildingEnergyRatingCertificateNumber = None,
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = None,
      sources = List(InformationSource.MaherPropertyIeAdvert(self)),
      facets = List(AdvertFacet(self.url)),
      advertiser = Option(AdvertiserStoreInMemory.maherPropertyIe),
      createdAt = self.createdAt
    )

  given JsonCodec[MaherPropertyIeAdvert] = DeriveJsonCodec.gen[MaherPropertyIeAdvert]
}
