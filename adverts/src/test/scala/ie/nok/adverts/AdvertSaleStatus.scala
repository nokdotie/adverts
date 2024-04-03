package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}

private val genAdvertSaleStatus: Gen[AdvertSaleStatus] =
  Gen.oneOf(AdvertSaleStatus.values.toSeq)

given Arbitrary[AdvertSaleStatus] = Arbitrary(genAdvertSaleStatus)
