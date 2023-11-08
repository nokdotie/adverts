package ie.nok.adverts.scraperV2.daftie
import ie.nok.adverts.scraperV2.daftie.DaftPropertyResponse.*
import zio.json.{DeriveJsonDecoder, JsonDecoder}

object DaftPropertyResponseJsonDecoders {

  protected[daftie] given JsonDecoder[DaftResponse] = DeriveJsonDecoder.gen[DaftResponse]

  protected[daftie] given JsonDecoder[Listings] = DeriveJsonDecoder.gen[Listings]

  protected[daftie] given JsonDecoder[Listing] = DeriveJsonDecoder.gen[Listing]

  protected[daftie] given JsonDecoder[FloorArea] = DeriveJsonDecoder.gen[FloorArea]

  protected[daftie] given JsonDecoder[Media] = DeriveJsonDecoder.gen[Media]

  protected[daftie] given JsonDecoder[Image] = DeriveJsonDecoder.gen[Image]

  protected[daftie] given JsonDecoder[Point] = DeriveJsonDecoder.gen[Point]

  protected[daftie] given JsonDecoder[Ber] = DeriveJsonDecoder.gen[Ber]

  protected[daftie] given JsonDecoder[Seller] = DeriveJsonDecoder.gen[Seller]

}
