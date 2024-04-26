package ie.nok.adverts

import zio.json.{DeriveJsonCodec, JsonCodec}

case class AdvertFacet(
    url: String
)

object AdvertFacet {
  given JsonCodec[AdvertFacet] = DeriveJsonCodec.gen[AdvertFacet]
}
