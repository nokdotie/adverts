package ie.nok.adverts.services.sherryfitzie

import ie.nok.adverts.{PropertyType, given}
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import java.time.Instant

private val genSherryFitzIeAdvert: Gen[SherryFitzIeAdvert] = for {
  url                                   <- arbitrary[String]
  priceInEur                            <- arbitrary[Option[Int]]
  description                           <- arbitrary[String]
  propertyType                          <- arbitrary[PropertyType].map(Some(_))
  address                               <- arbitrary[String]
  eircode                               <- arbitrary[Option[Eircode]]
  coordinates                           <- arbitrary[Coordinates]
  imageUrls                             <- arbitrary[List[String]]
  size                                  <- arbitrary[Option[Area]]
  bedroomsCount                         <- arbitrary[Option[Int]]
  bathroomsCount                        <- arbitrary[Option[Int]]
  buildingEnergyRating                  <- arbitrary[Option[Rating]]
  buildingEnergyRatingCertificateNumber <- arbitrary[Option[Int]]
  buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear <-
    arbitrary[Option[BigDecimal]]
  createdAt <- arbitrary[Instant]
  sherryFitzIeAdvert = SherryFitzIeAdvert(
    url = url,
    priceInEur = priceInEur,
    description = description,
    propertyType = propertyType,
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
    createdAt = createdAt
  )
} yield sherryFitzIeAdvert

given Arbitrary[SherryFitzIeAdvert] = Arbitrary(genSherryFitzIeAdvert)
