package ie.nok.advertisers

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val genAdvertiser: Gen[Advertiser] = for {
  id                                               <- arbitrary[String]
  name                                             <- arbitrary[String]
  pictureUrl                                       <- arbitrary[String]
  emailAddresses                                   <- arbitrary[List[String]]
  phoneNumbers                                     <- arbitrary[List[String]]
  physicalAddresses                                <- arbitrary[List[String]]
  propertyServicesRegulatoryAuthorityLicenceNumber <- arbitrary[String]
} yield Advertiser(
  id = id,
  name = name,
  pictureUrl = pictureUrl,
  emailAddresses = emailAddresses,
  phoneNumbers = phoneNumbers,
  physicalAddresses = physicalAddresses,
  propertyServicesRegulatoryAuthorityLicenceNumber = propertyServicesRegulatoryAuthorityLicenceNumber
)

given Arbitrary[Advertiser] = Arbitrary(genAdvertiser)
