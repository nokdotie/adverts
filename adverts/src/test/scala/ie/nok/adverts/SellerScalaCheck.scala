package ie.nok.adverts
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

val genSeller: Gen[Seller] = for {
  sellerId                                         <- arbitrary[String]
  name                                             <- arbitrary[String]
  phoneNumbers                                     <- arbitrary[List[String]]
  physicalAddress                                  <- arbitrary[Option[String]]
  propertyServicesRegulatoryAuthorityLicenceNumber <- arbitrary[Option[String]]
} yield Seller(
  sellerId = sellerId,
  name = name,
  phoneNumbers = phoneNumbers,
  physicalAddress = physicalAddress,
  propertyServicesRegulatoryAuthorityLicenceNumber = propertyServicesRegulatoryAuthorityLicenceNumber
)

given Arbitrary[Seller] = Arbitrary(genSeller)
