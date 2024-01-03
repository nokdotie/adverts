package ie.nok.adverts.services.dngie

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}

private val genDngIeAdvert: Gen[DngIeAdvert] = for {
  url <- arbitrary[String]
  priceInEur <- arbitrary[Option[Int]]
  description <- arbitrary[String]
  address <- arbitrary[String]
  eircode <- arbitrary[Option[Eircode]]
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
  dngIeAdvert = DngIeAdvert(
    url = url,
    priceInEur = priceInEur,
    description = description,
    address = address,
    eircode = eircode,
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
} yield dngIeAdvert

given Arbitrary[DngIeAdvert] = Arbitrary(genDngIeAdvert)
