package ie.nok.advertisers

import zio.json.{DeriveJsonCodec, JsonCodec}

case class Advertiser(
    id: String,
    name: String,
    pictureUrl: String,
    emailAddresses: List[String],
    phoneNumbers: List[String],
    physicalAddresses: List[String],
    propertyServicesRegulatoryAuthorityLicenceNumber: String
)

object Advertiser {
  given JsonCodec[Advertiser] = DeriveJsonCodec.gen[Advertiser]
}
