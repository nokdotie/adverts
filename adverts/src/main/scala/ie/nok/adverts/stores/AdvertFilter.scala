package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import math.Ordered.orderingToOrdered

enum AdvertFilter {
  case Empty
  case And(head: AdvertFilter, tail: AdvertFilter*)
  case Or(head: AdvertFilter, tail: AdvertFilter*)

  case PriceInEur(filter: NumericFilter[Int])
  case Address(filter: StringFilter)
  case GeoHash(filter: StringFilter)
  case SizeInSqtMtr(filter: NumericFilter[BigDecimal])
  case BedroomsCount(filter: NumericFilter[Int])
  case BathroomsCount(filter: NumericFilter[Int])

  def filter(value: Advert): Boolean = this match {
    case Empty                => true
    case And(head, tail @ _*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail @ _*)  => (head +: tail).exists(_.filter(value))

    case PriceInEur(filter)     => filter.filter(value.advertPriceInEur)
    case Address(filter)        => filter.filter(value.propertyAddress)
    case GeoHash(filter)        => filter.filter(value.propertyGeoHash)
    case SizeInSqtMtr(filter)   => filter.filter(value.propertySizeInSqtMtr)
    case BedroomsCount(filter)  => filter.filter(value.propertyBedroomsCount)
    case BathroomsCount(filter) => filter.filter(value.propertyBathroomsCount)
  }
}

enum StringFilter {
  case Empty
  case And(head: StringFilter, tail: StringFilter*)
  case Or(head: StringFilter, tail: StringFilter*)

  case Contains(filter: String) extends StringFilter
  case StartsWith(filter: String) extends StringFilter

  def filter(value: String): Boolean = this match {
    case Empty                => true
    case And(head, tail @ _*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail @ _*)  => (head +: tail).exists(_.filter(value))

    case Contains(filter) => value.toLowerCase().contains(filter.toLowerCase())
    case StartsWith(filter) =>
      value.toLowerCase().startsWith(filter.toLowerCase())
  }
}

enum NumericFilter[A: Numeric] {
  case Empty[A: Numeric]() extends NumericFilter[A]
  case And[A: Numeric](head: NumericFilter[A], tail: NumericFilter[A]*)
      extends NumericFilter[A]
  case Or[A: Numeric](head: NumericFilter[A], tail: NumericFilter[A]*)
      extends NumericFilter[A]

  case Equals[A: Numeric](filter: A) extends NumericFilter[A]
  case GreaterThan[A: Numeric](filter: A) extends NumericFilter[A]
  case LessThan[A: Numeric](filter: A) extends NumericFilter[A]

  def filter(value: A): Boolean = this match {
    case Empty()              => true
    case And(head, tail @ _*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail @ _*)  => (head +: tail).exists(_.filter(value))

    case Equals(filter)      => value == filter
    case GreaterThan(filter) => value > filter
    case LessThan(filter)    => value < filter
  }
}

object NumericFilter {
  def GreaterThanOrEqual[A: Numeric](filter: A): NumericFilter[A] =
    NumericFilter.Or(
      NumericFilter.GreaterThan(filter),
      NumericFilter.Equals(filter)
    )
  def LessThanOrEqual[A: Numeric](filter: A): NumericFilter[A] = NumericFilter
    .Or(NumericFilter.LessThan(filter), NumericFilter.Equals(filter))
}
