package ie.nok.adverts

import ie.nok.ber.Certificate
import zio.json.{JsonCodec, DeriveJsonCodec}

enum InformationSource {
  case DaftIeAdvert(value: services.daftie.DaftIeAdvert)
  case DngIeAdvert(value: services.dngie.DngIeAdvert)
  case MyHomeIeAdvert(value: services.myhomeie.MyHomeIeAdvert)
  case PropertyPalComAdvert(value: services.propertypalcom.PropertyPalComAdvert)
  case SherryFitzIeAdvert(value: services.sherryfitzie.SherryFitzIeAdvert)
  case MaherPropertyIeAdvert(value: services.maherpropertyie.MaherPropertyIeAdvert)
  case BuildingEnergyRatingCertificate(value: Certificate)
}

object InformationSource {
  given JsonCodec[InformationSource] = DeriveJsonCodec.gen[InformationSource]
}
