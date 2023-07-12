package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant
import ie.nok.unit.{Area, arbArea, Coordinates, arbCoordinates}

val genAdvert: Gen[Advert] = for {
  advertUrl <- arbitrary[String]
  advertPriceInEur <- arbitrary[Int]
  propertyAddress <- arbitrary[String]
  propertyCoordinates <- arbitrary[Coordinates]
  propertyImageUrls <- arbitrary[List[String]]
  propertySize <- arbitrary[Area]
  propertySizeInSqtMtr = Area.toSquareMetres(propertySize).value
  propertyBedroomsCount <- arbitrary[Int]
  propertyBathroomsCount <- arbitrary[Int]
  createdAt <- arbitrary[Instant]
  advert = Advert(
    advertUrl = advertUrl,
    advertPriceInEur = advertPriceInEur,
    propertyAddress = propertyAddress,
    propertyCoordinates = propertyCoordinates,
    propertyImageUrls = propertyImageUrls,
    propertySize = propertySize,
    propertySizeInSqtMtr = propertySizeInSqtMtr,
    propertyBedroomsCount = propertyBedroomsCount,
    propertyBathroomsCount = propertyBathroomsCount,
    createdAt = createdAt
  )
} yield advert

implicit val arbArbitrary: Arbitrary[Advert] =
  Arbitrary(genAdvert)
