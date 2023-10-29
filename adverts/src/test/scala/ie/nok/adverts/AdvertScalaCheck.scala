package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}

private val genAdvert: Gen[Advert] = for {
  advertUrl <- arbitrary[String]
  advertPriceInEur <- arbitrary[Int]
  propertyIdentifier <- arbitrary[String]
  propertyAddress <- arbitrary[String]
  propertyEircode <- arbitrary[Option[Eircode]]
  propertyCoordinates <- arbitrary[Coordinates]
  propertyImageUrls <- arbitrary[List[String]]
  propertySize <- arbitrary[Area]
  propertySizeInSqtMtr = Area.toSquareMetres(propertySize).value
  propertyBedroomsCount <- arbitrary[Int]
  propertyBathroomsCount <- arbitrary[Int]
  propertyBuildingEnergyRating <- arbitrary[Rating].map { Some(_) }
  propertyBuildingEnergyRatingCertificateNumber <- arbitrary[Option[Int]]
  propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear <-
    arbitrary[Option[BigDecimal]]
  sources <- arbitrary[List[InformationSource]]
  createdAt <- arbitrary[Instant]
  advert = Advert(
    advertUrl = advertUrl,
    advertPriceInEur = advertPriceInEur,
    propertyIdentifier = propertyIdentifier,
    propertyAddress = propertyAddress,
    propertyEircode = propertyEircode,
    propertyCoordinates = propertyCoordinates,
    propertyImageUrls = propertyImageUrls,
    propertySize = propertySize,
    propertySizeInSqtMtr = propertySizeInSqtMtr,
    propertyBedroomsCount = propertyBedroomsCount,
    propertyBathroomsCount = propertyBathroomsCount,
    propertyBuildingEnergyRating = propertyBuildingEnergyRating,
    propertyBuildingEnergyRatingCertificateNumber =
      propertyBuildingEnergyRatingCertificateNumber,
    propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear =
      propertyBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear,
    sources = sources,
    createdAt = createdAt
  )
} yield advert

given Arbitrary[Advert] =
  Arbitrary(genAdvert)
