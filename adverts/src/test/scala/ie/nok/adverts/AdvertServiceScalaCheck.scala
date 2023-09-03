package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}

private val genAdvertService: Gen[AdvertService] =
  Gen.oneOf(AdvertService.values)

given Arbitrary[AdvertService] = Arbitrary(genAdvertService)
