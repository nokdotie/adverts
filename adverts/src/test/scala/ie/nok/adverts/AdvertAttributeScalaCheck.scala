package ie.nok.adverts

import ie.nok.geographic.{Coordinates, given}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary

private val genAdvertAttribute: Gen[AdvertAttribute] = {
  val genPriceInEur = for {
    value <- arbitrary[Int]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.PriceInEur(value, source)

  val genAddress = for {
    value <- arbitrary[String]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.Address(value, source)

  val genCoordinates = for {
    value <- arbitrary[Coordinates]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.Coordinates(value, source)

  val genImageUrl = for {
    value <- arbitrary[String]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.ImageUrl(value, source)

  val genSizeInSqtMtr = for {
    value <- arbitrary[BigDecimal]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.SizeInSqtMtr(value, source)

  val genBedroomsCount = for {
    value <- arbitrary[Int]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.BedroomsCount(value, source)

  val genBathroomsCount = for {
    value <- arbitrary[Int]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.BathroomsCount(value, source)

  val genBuildingEnergyRating = for {
    value <- arbitrary[String]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.BuildingEnergyRating(value, source)

  val genBuildingEnergyRatingCertificateNumber = for {
    value <- arbitrary[Int]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.BuildingEnergyRatingCertificateNumber(value, source)

  val getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = for {
    value <- arbitrary[BigDecimal]
    source <- arbitrary[AdvertSource]
  } yield AdvertAttribute.BuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(
    value,
    source
  )

  Gen.oneOf(
    genPriceInEur,
    genAddress,
    genCoordinates,
    genImageUrl,
    genSizeInSqtMtr,
    genBedroomsCount,
    genBathroomsCount,
    genBuildingEnergyRating,
    genBuildingEnergyRatingCertificateNumber,
    getBuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear
  )
}

given Arbitrary[AdvertAttribute] = Arbitrary(genAdvertAttribute)
