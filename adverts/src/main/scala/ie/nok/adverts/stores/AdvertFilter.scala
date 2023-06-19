package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import math.Ordered.orderingToOrdered

enum AdvertFilter {
    case Empty
    case And(left: AdvertFilter, right: AdvertFilter)
    case Or(left: AdvertFilter, right: AdvertFilter)

    case Price(filter: NumericFilter[Int])
    case Address(filter: StringFilter)
    case SizeinSqtMtr(filter: NumericFilter[BigDecimal])
    case BedroomsCount(filter: NumericFilter[Int])
    case BathroomsCount(filter: NumericFilter[Int])

    def filter(advert: Advert): Boolean = this match {
        case Empty => true
        case And(left, right) => left.filter(advert) && right.filter(advert)
        case Or(left, right) => left.filter(advert) || right.filter(advert)
        case Price(filter) => filter.filter(advert.advertPrice)
        case Address(filter) => filter.filter(advert.propertyAddress)
        case SizeinSqtMtr(filter) => filter.filter(advert.propertySizeinSqtMtr)
        case BedroomsCount(filter) => filter.filter(advert.propertyBedroomsCount)
        case BathroomsCount(filter) => filter.filter(advert.propertyBathroomsCount)
    }
}

enum StringFilter {
    case Contains(filter: String) extends StringFilter

    def filter(value: String): Boolean = this match {
        case Contains(filter) => value.toLowerCase().contains(filter.toLowerCase())
    }
}

enum NumericFilter[A: Numeric] {
    case Equals[A: Numeric](filter: A) extends NumericFilter[A]
    case GreaterThan[A: Numeric](filter: A) extends NumericFilter[A]
    case LessThan[A: Numeric](filter: A) extends NumericFilter[A]

    def filter(value: A): Boolean = this match {
        case Equals(filter) => filter == value
        case GreaterThan(filter) => filter > value
        case LessThan(filter) => filter < value
    }
}
