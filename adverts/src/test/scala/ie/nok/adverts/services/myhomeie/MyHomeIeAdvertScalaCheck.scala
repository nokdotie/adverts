package ie.nok.adverts.services.myhomeie

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant
import ie.nok.ber.{Rating, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}

private val genMyHomeIeAdvert: Gen[MyHomeIeAdvert] = for {
  url <- arbitrary[String]
  priceInEur <- arbitrary[Option[Int]]
  address <- arbitrary[String]
  coordinates <- arbitrary[Option[Coordinates]]
  imageUrls <- arbitrary[List[String]]
  size <- arbitrary[Option[Area]]
  bedroomsCount <- arbitrary[Option[Int]]
  bathroomsCount <- arbitrary[Option[Int]]
  buildingEnergyRating <- arbitrary[Option[Rating]]
  createdAt <- arbitrary[Instant]
  myHomeIeAdvert = MyHomeIeAdvert(
    url = url,
    priceInEur = priceInEur,
    address = address,
    coordinates = coordinates,
    imageUrls = imageUrls,
    size = size,
    bedroomsCount = bedroomsCount,
    bathroomsCount = bathroomsCount,
    buildingEnergyRating = buildingEnergyRating,
    createdAt = createdAt
  )
} yield myHomeIeAdvert

given Arbitrary[MyHomeIeAdvert] = Arbitrary(genMyHomeIeAdvert)
