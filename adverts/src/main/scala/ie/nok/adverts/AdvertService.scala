package ie.nok.adverts

enum AdvertService(val host: String) {
  case DaftIe extends AdvertService("daft.ie")
  case MyHomeIe extends AdvertService("myhome.ie")
  case PropertyPalCom extends AdvertService("propertypal.com")
  case DooglasNewmanGoodIe extends AdvertService("dng.ie")
  case SherryFitzgeraldIe extends AdvertService("sherryfitz.ie")
}
