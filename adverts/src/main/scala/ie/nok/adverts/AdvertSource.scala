package ie.nok.adverts

import zio.json.{JsonCodec, DeriveJsonCodec}

case class AdvertSource(service: AdvertService, url: String)

object AdvertSource {
  given JsonCodec[AdvertSource] = DeriveJsonCodec.gen[AdvertSource]
}
