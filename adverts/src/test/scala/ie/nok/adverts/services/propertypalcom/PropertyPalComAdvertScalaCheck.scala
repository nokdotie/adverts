package ie.nok.adverts.services.propertypalcom

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}

private val genPropertyPalComAdvert: Gen[PropertyPalComAdvert] = for {
  url <- arbitrary[String]
  priceInEur <- arbitrary[Option[Int]]
  address <- arbitrary[String]
  eircode <- arbitrary[Option[Eircode]]
  coordinates <- arbitrary[Option[Coordinates]]
  imageUrls <- arbitrary[List[String]]
  size <- arbitrary[Option[Area]]
  bedroomsCount <- arbitrary[Option[Int]]
  bathroomsCount <- arbitrary[Option[Int]]
  buildingEnergyRating <- arbitrary[Option[Rating]]
  buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear <-
    arbitrary[Option[BigDecimal]]
  createdAt <- arbitrary[Instant]
  propertyPalComAdvert = PropertyPalComAdvert(
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
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
    createdAt = createdAt
  )
} yield propertyPalComAdvert

given Arbitrary[PropertyPalComAdvert] = Arbitrary(genPropertyPalComAdvert)
