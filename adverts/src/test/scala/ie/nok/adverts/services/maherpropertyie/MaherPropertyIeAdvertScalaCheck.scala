package ie.nok.adverts.services.maherpropertyie

import ie.nok.adverts.{AdvertSaleStatus, PropertyType, given}
import ie.nok.ber.{Rating, given}
import ie.nok.ecad.{Eircode, given}
import ie.nok.geographic.{Coordinates, given}
import ie.nok.unit.{Area, given}
import java.time.Instant
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val gen: Gen[MaherPropertyIeAdvert] = for {
  url            <- arbitrary[String]
  saleStatus     <- arbitrary[AdvertSaleStatus]
  priceInEur     <- arbitrary[Option[Int]]
  description    <- arbitrary[String]
  address        <- arbitrary[String]
  eircode        <- arbitrary[Option[Eircode]]
  imageUrls      <- arbitrary[List[String]]
  sizeInSqtMtr   <- arbitrary[Option[Int]]
  bedroomsCount  <- arbitrary[Option[Int]]
  bathroomsCount <- arbitrary[Option[Int]]
  createdAt      <- arbitrary[Instant]
  advert = MaherPropertyIeAdvert(
    url = url,
    saleStatus = saleStatus,
    priceInEur = priceInEur,
    description = description,
    address = address,
    eircode = eircode,
    imageUrls = imageUrls,
    sizeInSqtMtr = sizeInSqtMtr,
    bedroomsCount = bedroomsCount,
    bathroomsCount = bathroomsCount,
    createdAt = createdAt
  )
} yield advert

given Arbitrary[MaherPropertyIeAdvert] = Arbitrary(gen)
