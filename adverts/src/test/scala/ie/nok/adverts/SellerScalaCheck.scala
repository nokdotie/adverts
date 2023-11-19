package ie.nok.adverts
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

val genSeller: Gen[Seller] = for {
  sellerId         <- arbitrary[String]
  name             <- arbitrary[String]
  phone            <- arbitrary[Option[String]]
  alternativePhone <- arbitrary[Option[String]]
  address          <- arbitrary[Option[String]]
  licenseNumber    <- arbitrary[Option[String]]
} yield Seller(
  sellerId = sellerId,
  name = name,
  phone = phone,
  alternativePhone = alternativePhone,
  address = address,
  licenceNumber = licenseNumber
)

given Arbitrary[Seller] = Arbitrary(genSeller)
