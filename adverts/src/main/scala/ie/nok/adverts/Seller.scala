package ie.nok.adverts
import zio.json.{DeriveJsonCodec, JsonCodec}

case class Seller(
    sellerId: String,
    name: String,
    phoneNumbers: List[String],
    physicalAddress: Option[String],
    propertyServicesRegulatoryAuthorityLicenceNumber: Option[String]
)

object Seller {
  given JsonCodec[Seller] = DeriveJsonCodec.gen[Seller]
}
