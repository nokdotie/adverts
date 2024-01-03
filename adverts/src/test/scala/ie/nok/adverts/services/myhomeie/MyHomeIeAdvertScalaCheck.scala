package ie.nok.adverts.services.myhomeie

import ie.nok.advertisers.{Advertiser, given}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant
import ie.nok.ber.{Rating, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}

private val genMyHomeIeAdvert: Gen[MyHomeIeAdvert] = for {
  url <- arbitrary[String]
  priceInEur <- arbitrary[Option[Int]]
  description <- arbitrary[Option[String]]
  address <- arbitrary[String]
  coordinates <- arbitrary[Option[Coordinates]]
  imageUrls <- arbitrary[List[String]]
  size <- arbitrary[Option[Area]]
  bedroomsCount <- arbitrary[Option[Int]]
  bathroomsCount <- arbitrary[Option[Int]]
  buildingEnergyRating <- arbitrary[Option[Rating]]
  advertiser <- arbitrary[Option[Advertiser]]
  createdAt <- arbitrary[Instant]
  myHomeIeAdvert = MyHomeIeAdvert(
    url = url,
    priceInEur = priceInEur,
    description = description,
    address = address,
    coordinates = coordinates,
    imageUrls = imageUrls,
    size = size,
    bedroomsCount = bedroomsCount,
    bathroomsCount = bathroomsCount,
    buildingEnergyRating = buildingEnergyRating,
    advertiser = advertiser,
    createdAt = createdAt
  )
} yield myHomeIeAdvert

given Arbitrary[MyHomeIeAdvert] = Arbitrary(genMyHomeIeAdvert)
