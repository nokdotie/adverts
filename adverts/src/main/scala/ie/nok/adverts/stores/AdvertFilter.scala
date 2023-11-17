package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.geographic.Coordinates
import math.Ordered.orderingToOrdered

enum AdvertFilter {
  case Empty
  case And(head: AdvertFilter, tail: AdvertFilter*)
  case Or(head: AdvertFilter, tail: AdvertFilter*)

  case AdvertPriceInEur(filter: NumericFilter[Int])
  case PropertyIdentifier(filter: StringFilter)
  case PropertyAddress(filter: StringFilter)
  case PropertyCoordinates(filter: CoordinatesFilter)
  case PropertySizeInSqtMtr(filter: NumericFilter[BigDecimal])
  case PropertyBedroomsCount(filter: NumericFilter[Int])
  case PropertyBathroomsCount(filter: NumericFilter[Int])

  def filter(value: Advert): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case AdvertPriceInEur(filter)    => filter.filter(value.advertPriceInEur)
    case PropertyIdentifier(filter)  => filter.filter(value.propertyIdentifier)
    case PropertyAddress(filter)     => filter.filter(value.propertyAddress)
    case PropertyCoordinates(filter) => filter.filter(value.propertyCoordinates)
    case PropertySizeInSqtMtr(filter) =>
      filter.filter(value.propertySizeInSqtMtr)
    case PropertyBedroomsCount(filter) =>
      filter.filter(value.propertyBedroomsCount)
    case PropertyBathroomsCount(filter) =>
      filter.filter(value.propertyBathroomsCount)
  }
}

enum StringFilter {
  case Empty
  case And(head: StringFilter, tail: StringFilter*)
  case Or(head: StringFilter, tail: StringFilter*)

  case Equals(filter: String)
  case ContainsCaseInsensitive(filter: String)
  case StartsWithCaseInsensitive(filter: String)

  def filter(value: String): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case Equals(filter) => value == filter
    case ContainsCaseInsensitive(filter) =>
      value.toLowerCase().contains(filter.toLowerCase())
    case StartsWithCaseInsensitive(filter) =>
      value.toLowerCase().startsWith(filter.toLowerCase())
  }
}

enum NumericFilter[A: Numeric] {
  case Empty[A: Numeric]()                                              extends NumericFilter[A]
  case And[A: Numeric](head: NumericFilter[A], tail: NumericFilter[A]*) extends NumericFilter[A]
  case Or[A: Numeric](head: NumericFilter[A], tail: NumericFilter[A]*)  extends NumericFilter[A]

  case Equals[A: Numeric](filter: A)      extends NumericFilter[A]
  case GreaterThan[A: Numeric](filter: A) extends NumericFilter[A]
  case LessThan[A: Numeric](filter: A)    extends NumericFilter[A]

  def filter(value: A): Boolean = this match {
    case Empty()          => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

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

enum CoordinatesFilter {
  case Empty
  case And(head: CoordinatesFilter, tail: CoordinatesFilter*)
  case Or(head: CoordinatesFilter, tail: CoordinatesFilter*)

  case WithinRectangle(northEast: Coordinates, southWest: Coordinates)

  def filter(value: Coordinates): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case WithinRectangle(northEast, southWest) =>
      value.latitude <= northEast.latitude && value.latitude >= southWest.latitude &&
      value.longitude <= northEast.longitude && value.longitude >= southWest.longitude
  }
}
