package ie.nok.adverts

import zio.json.{JsonCodec, DeriveJsonCodec}

enum AdvertSaleStatus {
  case ForSale
  case SaleAgreed
  case Sold
}

object AdvertSaleStatus {
  given JsonCodec[AdvertSaleStatus] = DeriveJsonCodec.gen[AdvertSaleStatus]
}
