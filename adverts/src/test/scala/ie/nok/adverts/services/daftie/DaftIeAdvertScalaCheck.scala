package ie.nok.adverts.services.daftie

import ie.nok.adverts.{Seller, given}
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import java.time.Instant

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
  seller    <- arbitrary[Seller]
  createdAt <- arbitrary[Instant]
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
    seller = Some(seller),
    createdAt = createdAt
  )
} yield daftIeAdvert

given Arbitrary[DaftIeAdvert] = Arbitrary(genDaftIeAdvert)
