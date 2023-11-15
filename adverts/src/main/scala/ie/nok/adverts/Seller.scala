package ie.nok.adverts
import zio.json.{DeriveJsonCodec, JsonCodec}

case class Seller(
    sellerId: Int,
    name: String,
    phone: String,
    alternativePhone: Option[String],
    address: Option[String],
    licenceNumber: Option[String]
)

object Seller {
  given JsonCodec[Seller] = DeriveJsonCodec.gen[Seller]
}
