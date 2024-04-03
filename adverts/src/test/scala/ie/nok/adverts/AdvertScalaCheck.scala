package ie.nok.adverts

import ie.nok.advertisers.{Advertiser, given}
import ie.nok.adverts.given
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import java.time.Instant

private val genAdvert: Gen[Advert] = for {
  advertUrl           <- arbitrary[String]
  advertSaleStatus    <- arbitrary[AdvertSaleStatus]
  advertPriceInEur    <- arbitrary[Int]
  propertyIdentifier  <- arbitrary[String]
  propertyDescription <- arbitrary[Option[String]]
  propertyType        <- arbitrary[PropertyType].map(Some(_))
  propertyAddress     <- arbitrary[String]
  propertyEircode     <- arbitrary[Option[Eircode]]
  propertyCoordinates <- arbitrary[Coordinates]
  propertyImageUrls   <- arbitrary[List[String]]
  propertySize        <- arbitrary[Area]
  propertySizeInSqtMtr = Area.toSquareMetres(propertySize).value
  propertyBedroomsCount                         <- arbitrary[Int]
  propertyBathroomsCount                        <- arbitrary[Int]
  propertyBuildingEnergyRating                  <- arbitrary[Rating].map { Some(_) }
  propertyBuildingEnergyRatingCertificateNumber <- arbitrary[Option[Int]]
  propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear <-
    arbitrary[Option[BigDecimal]]
  sources    <- arbitrary[List[InformationSource]]
  advertiser <- arbitrary[Option[Advertiser]]
  createdAt  <- arbitrary[Instant]
  advert = Advert(
    advertUrl = advertUrl,
    advertSaleStatus = advertSaleStatus,
    advertPriceInEur = advertPriceInEur,
    propertyIdentifier = propertyIdentifier,
    propertyDescription = propertyDescription,
    propertyType = propertyType,
    propertyAddress = propertyAddress,
    propertyEircode = propertyEircode,
    propertyCoordinates = propertyCoordinates,
    propertyImageUrls = propertyImageUrls,
    propertySize = propertySize,
    propertySizeInSqtMtr = propertySizeInSqtMtr,
    propertyBedroomsCount = propertyBedroomsCount,
    propertyBathroomsCount = propertyBathroomsCount,
    propertyBuildingEnergyRating = propertyBuildingEnergyRating,
    propertyBuildingEnergyRatingCertificateNumber = propertyBuildingEnergyRatingCertificateNumber,
    propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
    sources = sources,
    advertiser = advertiser,
    createdAt = createdAt
  )
} yield advert

given Arbitrary[Advert] =
  Arbitrary(genAdvert)
