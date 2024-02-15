package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.geographic.Coordinates
import zio.json.{DeriveJsonCodec, JsonCodec}

enum AdvertFilter {
  case Empty
  case And(head: AdvertFilter, tail: AdvertFilter*)
  case Or(head: AdvertFilter, tail: AdvertFilter*)

  case AdvertPriceInEur(filter: IntFilter)
  case PropertyIdentifier(filter: StringFilter)
  case PropertyAddress(filter: StringFilter)
  case PropertyCoordinates(filter: CoordinatesFilter)
  case PropertySizeInSqtMtr(filter: IntFilter)
  case PropertyBedroomsCount(filter: IntFilter)
  case PropertyBathroomsCount(filter: IntFilter)
  case PropertyType(filter: StringFilter)

  def filter(value: Advert): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case AdvertPriceInEur(filter)       => filter.filter(value.advertPriceInEur)
    case PropertyIdentifier(filter)     => filter.filter(value.propertyIdentifier)
    case PropertyAddress(filter)        => filter.filter(value.propertyAddress)
    case PropertyCoordinates(filter)    => filter.filter(value.propertyCoordinates)
    case PropertySizeInSqtMtr(filter)   => filter.filter(value.propertySizeInSqtMtr.toInt)
    case PropertyBedroomsCount(filter)  => filter.filter(value.propertyBedroomsCount)
    case PropertyBathroomsCount(filter) => filter.filter(value.propertyBathroomsCount)
    case PropertyType(filter)           => value.propertyType.exists(p => filter.filter(p.toString))
  }
}

object AdvertFilter {
  given JsonCodec[AdvertFilter] = DeriveJsonCodec.gen[AdvertFilter]
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

object StringFilter {
  given JsonCodec[StringFilter] = DeriveJsonCodec.gen[StringFilter]
}

enum IntFilter {
  case Empty
  case And(head: IntFilter, tail: IntFilter*)
  case Or(head: IntFilter, tail: IntFilter*)

  case Equals(filter: Int)
  case GreaterThan(filter: Int)
  case LessThan(filter: Int)

  def filter(value: Int): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case Equals(filter)      => value == filter
    case GreaterThan(filter) => value > filter
    case LessThan(filter)    => value < filter
  }
}

object IntFilter {
  given JsonCodec[IntFilter] = DeriveJsonCodec.gen[IntFilter]

  def GreaterThanOrEqual(filter: Int): IntFilter =
    IntFilter.Or(
      IntFilter.GreaterThan(filter),
      IntFilter.Equals(filter)
    )
  def LessThanOrEqual(filter: Int): IntFilter = IntFilter
    .Or(IntFilter.LessThan(filter), IntFilter.Equals(filter))
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

object CoordinatesFilter {
  given JsonCodec[CoordinatesFilter] = DeriveJsonCodec.gen[CoordinatesFilter]
}
