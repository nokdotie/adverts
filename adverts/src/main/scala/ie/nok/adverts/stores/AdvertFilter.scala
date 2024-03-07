package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.geographic.{Coordinates, CoordinatesFilter}
import zio.json.{DeriveJsonCodec, JsonCodec}
import ie.nok.filter.{IntFilter, StringFilter}

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
