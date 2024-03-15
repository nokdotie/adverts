package ie.nok.adverts

object AdvertUrl {

  // Beware to keep this in sync with the actual URL
  def fromAdvert(advert: Advert): String =
    s"https://nok.ie/properties/${advert.propertyIdentifier}"

}
