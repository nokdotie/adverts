package ie.nok.adverts

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val genAdvertFacet: Gen[AdvertFacet] = for {
  url <- arbitrary[String]
  advertFacet = AdvertFacet(url)
} yield advertFacet

given Arbitrary[AdvertFacet] =
  Arbitrary(genAdvertFacet)
