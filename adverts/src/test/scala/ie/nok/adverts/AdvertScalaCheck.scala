package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant

val genAdvert: Gen[Advert] = for {
  advertUrl <- arbitrary[String]
  advertPrice <- arbitrary[Int]
  propertyAddress <- arbitrary[String]
  propertyImageUrls <- arbitrary[List[String]]
  propertySizeinSqtMtr <- arbitrary[BigDecimal]
  propertyBedroomsCount <- arbitrary[Int]
  propertyBathroomsCount <- arbitrary[Int]
  createdAt <- arbitrary[Instant]
  advert = Advert(
    advertUrl = advertUrl,
    advertPrice = advertPrice,
    propertyAddress = propertyAddress,
    propertyImageUrls = propertyImageUrls,
    propertySizeinSqtMtr = propertySizeinSqtMtr,
    propertyBedroomsCount = propertyBedroomsCount,
    propertyBathroomsCount = propertyBathroomsCount,
    createdAt = createdAt
  )
} yield advert

implicit val arbArbitrary: Arbitrary[Advert] =
  Arbitrary(genAdvert)
