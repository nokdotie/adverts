package ie.nok.adverts.scraperV2.daftie

import zio.json.{DeriveJsonCodec, JsonCodec}

case class Seller(
    sellerId: Int,
    name: String,
    phone: String,
    alternativePhone: Option[String],
    address: Option[String],
    branch: Option[String],
    profileImage: Option[String],
    standardLogo: Option[String],
    squareLogo: Option[String],
    licenceNumber: Option[String]
)

object Seller {

  given JsonCodec[Seller] = DeriveJsonCodec.gen[Seller]
}
