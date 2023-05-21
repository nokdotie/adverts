package ie.nok.adverts.utils

object Eircode {
  // https://www.citizensinformation.ie/en/consumer/phone_internet_tv_and_postal_services/eircode.html
  val regex = "([A-Z][0-9]{2}|D6W) ?[A-Z0-9]{4}".r
}
