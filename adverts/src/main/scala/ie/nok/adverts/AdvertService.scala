package ie.nok.adverts

enum AdvertService(val host: String) {
  case DaftIe extends AdvertService("daft.ie")
  case DngIe extends AdvertService("dng.ie")
  case MyHomeIe extends AdvertService("myhome.ie")
  case PropertyPalCom extends AdvertService("propertypal.com")
  case SherryFitzIe extends AdvertService("sherryfitz.ie")
}
