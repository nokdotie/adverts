package ie.nok.adverts

import zio.json.{JsonCodec, DeriveJsonCodec}

enum AdvertService(val host: String) {
  case AbbeyPropertySalesCom extends AdvertService("abbeypropertysales.com")
  case AlanBrowneEstatesIe   extends AdvertService("alanbrowneestates.ie")
  case AllenAndJacobsIe      extends AdvertService("allenandjacobs.ie")
  case AmoveIe               extends AdvertService("amove.ie")
  case DaftIe                extends AdvertService("daft.ie")
  case DngIe                 extends AdvertService("dng.ie")
  case MyHomeIe              extends AdvertService("myhome.ie")
  case PropertyPalCom        extends AdvertService("propertypal.com")
  case SherryFitzIe          extends AdvertService("sherryfitz.ie")
  case MaherPropertyIe       extends AdvertService("maherproperty.ie")
  case FiveSIe               extends AdvertService("5s.ie")
}

object AdvertService {
  given JsonCodec[AdvertService] = DeriveJsonCodec.gen[AdvertService]
}
