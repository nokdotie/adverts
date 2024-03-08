package ie.nok.adverts

object AdvertUrl {

  def fromAdvert(advert: Advert): String =
    s"https://nok.ie/properties/${advert.propertyIdentifier}"

}
