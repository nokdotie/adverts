package ie.nok.adverts.services.daftie

import ie.nok.advertisers.{Advertiser, given}
import ie.nok.adverts.given
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}
import java.time.Instant
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val genDaftIeAdvert: Gen[DaftIeAdvert] = for {
  url                                   <- arbitrary[String]
  priceInEur                            <- arbitrary[Option[Int]]
  address                               <- arbitrary[String]
  eircode                               <- arbitrary[Option[Eircode]]
  coordinates                           <- arbitrary[Coordinates]
  imageUrls                             <- arbitrary[List[String]]
  size                                  <- arbitrary[Option[Area]]
  bedroomsCount                         <- arbitrary[Option[Int]]
  bathroomsCount                        <- arbitrary[Option[Int]]
  buildingEnergyRating                  <- arbitrary[Option[Rating]]
  buildingEnergyRatingCertificateNumber <- arbitrary[Option[Int]]
  buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear <- arbitrary[Option[
    BigDecimal
  ]]
  advertiser <- arbitrary[Option[Advertiser]]
  createdAt  <- arbitrary[Instant]
  daftIeAdvert = DaftIeAdvert(
    url = url,
    priceInEur = priceInEur,
    address = address,
    eircode = eircode,
    coordinates = coordinates,
    imageUrls = imageUrls,
    size = size,
    bedroomsCount = bedroomsCount,
    bathroomsCount = bathroomsCount,
    buildingEnergyRating = buildingEnergyRating,
    buildingEnergyRatingCertificateNumber = buildingEnergyRatingCertificateNumber,
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
    advertiser = advertiser,
    createdAt = createdAt
  )
} yield daftIeAdvert

given Arbitrary[DaftIeAdvert] = Arbitrary(genDaftIeAdvert)
