package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}

private val genRating: Gen[PropertyType] =
  Gen.oneOf(PropertyType.values.toSeq)

given Arbitrary[PropertyType] = Arbitrary(genRating)
