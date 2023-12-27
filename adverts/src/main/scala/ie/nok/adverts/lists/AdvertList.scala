package ie.nok.adverts.lists

import ie.nok.adverts.stores.AdvertFilter
import zio.json.{DeriveJsonCodec, JsonCodec}

case class AdvertList(
    identifier: String,
    label: String,
    filter: AdvertFilter,
)

object AdvertList {
  given JsonCodec[AdvertList] = DeriveJsonCodec.gen[AdvertList]
}
