package ie.nok.adverts.stores

import ie.nok.unit.{Direction, given}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val genAdvertSortableField: Gen[AdvertSortableField] =
  Gen.oneOf(AdvertSortableField.values)

given Arbitrary[AdvertSortableField] =
  Arbitrary(genAdvertSortableField)

private val genAdvertSort: Gen[AdvertSort] = for {
  field     <- arbitrary[AdvertSortableField]
  direction <- arbitrary[Direction]
  advertSort = AdvertSort(field, direction)
} yield advertSort

given Arbitrary[AdvertSort] =
  Arbitrary(genAdvertSort)
