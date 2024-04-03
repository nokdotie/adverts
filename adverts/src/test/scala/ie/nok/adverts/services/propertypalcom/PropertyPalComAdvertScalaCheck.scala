package ie.nok.adverts.services.propertypalcom

import ie.nok.advertisers.{Advertiser, given}
import ie.nok.adverts.{AdvertSaleStatus, PropertyType, given}
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import java.time.Instant

private val genPropertyPalComAdvert: Gen[PropertyPalComAdvert] = for {
  url                  <- arbitrary[String]
  saleStatus           <- arbitrary[AdvertSaleStatus]
  priceInEur           <- arbitrary[Option[Int]]
  address              <- arbitrary[String]
  description          <- arbitrary[Option[String]]
  propertyType         <- arbitrary[PropertyType].map(Some(_))
  eircode              <- arbitrary[Option[Eircode]]
  coordinates          <- arbitrary[Option[Coordinates]]
  imageUrls            <- arbitrary[List[String]]
  size                 <- arbitrary[Option[Area]]
  bedroomsCount        <- arbitrary[Option[Int]]
  bathroomsCount       <- arbitrary[Option[Int]]
  buildingEnergyRating <- arbitrary[Option[Rating]]
  buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear <-
    arbitrary[Option[BigDecimal]]
  advertiser <- arbitrary[Option[Advertiser]]
  createdAt  <- arbitrary[Instant]
  propertyPalComAdvert = PropertyPalComAdvert(
    url = url,
    saleStatus = saleStatus,
    priceInEur = priceInEur,
    address = address,
    description = description,
    propertyType = propertyType,
    eircode = eircode,
    coordinates = coordinates,
    imageUrls = imageUrls,
    size = size,
    bedroomsCount = bedroomsCount,
    bathroomsCount = bathroomsCount,
    buildingEnergyRating = buildingEnergyRating,
    buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
    advertiser = advertiser,
    createdAt = createdAt
  )
} yield propertyPalComAdvert

given Arbitrary[PropertyPalComAdvert] = Arbitrary(genPropertyPalComAdvert)
