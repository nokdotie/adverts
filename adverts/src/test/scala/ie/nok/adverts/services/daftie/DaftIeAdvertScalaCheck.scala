package ie.nok.adverts.services.daftie

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant
import ie.nok.ber.{Rating, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}

private val genDaftIeAdvert: Gen[DaftIeAdvert] = for {
  url <- arbitrary[String]
  priceInEur <- arbitrary[Option[Int]]
  address <- arbitrary[String]
  coordinates <- arbitrary[Coordinates]
  imageUrls <- arbitrary[List[String]]
  size <- arbitrary[Option[Area]]
  bedroomsCount <- arbitrary[Option[Int]]
  bathroomsCount <- arbitrary[Option[Int]]
  buildingEnergyRating <- arbitrary[Option[Rating]]
  buildingEnergyRatingCertificateNumber <- arbitrary[Option[Int]]
  buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear <-
    arbitrary[Option[BigDecimal]]
  createdAt <- arbitrary[Instant]
  daftIeAdvert = DaftIeAdvert(
    url = url,
    priceInEur = priceInEur,
    address = address,
    coordinates = coordinates,
    imageUrls = imageUrls,
    size = size,
    bedroomsCount = bedroomsCount,
    bathroomsCount = bathroomsCount,
    buildingEnergyRating = buildingEnergyRating,
    buildingEnergyRatingCertificateNumber =
      buildingEnergyRatingCertificateNumber,
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
    createdAt = createdAt
  )
} yield daftIeAdvert

given Arbitrary[DaftIeAdvert] = Arbitrary(genDaftIeAdvert)
