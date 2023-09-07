package ie.nok.adverts

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import ie.nok.adverts.services.daftie.{DaftIeAdvert, given}
import ie.nok.adverts.services.dngie.{DngIeAdvert, given}
import ie.nok.adverts.services.myhomeie.{MyHomeIeAdvert, given}
import ie.nok.adverts.services.propertypalcom.{PropertyPalComAdvert, given}
import ie.nok.adverts.services.sherryfitzie.{SherryFitzIeAdvert, given}
import ie.nok.ber.{Certificate, given}

private val genInformationSource: Gen[InformationSource] = Gen.oneOf(
  arbitrary[DaftIeAdvert].map { InformationSource.DaftIeAdvert(_) },
  arbitrary[DngIeAdvert].map { InformationSource.DngIeAdvert(_) },
  arbitrary[MyHomeIeAdvert].map { InformationSource.MyHomeIeAdvert(_) },
  arbitrary[PropertyPalComAdvert].map {
    InformationSource.PropertyPalComAdvert(_)
  },
  arbitrary[SherryFitzIeAdvert].map { InformationSource.SherryFitzIeAdvert(_) },
  arbitrary[Certificate].map {
    InformationSource.BuildingEnergyRatingCertificate(_)
  }
)

given Arbitrary[InformationSource] = Arbitrary(genInformationSource)
