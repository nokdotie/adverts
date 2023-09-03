package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary

private val genAdvertSource: Gen[AdvertSource] = for {
  service <- arbitrary[AdvertService]
  url <- arbitrary[String]
} yield AdvertSource(service, url)

given Arbitrary[AdvertSource] = Arbitrary(genAdvertSource)
