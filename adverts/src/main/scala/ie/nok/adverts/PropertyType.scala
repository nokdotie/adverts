package ie.nok.adverts

import zio.json.{DeriveJsonCodec, JsonCodec}

import scala.util.{Failure, Success, Try}

enum PropertyType {
  case Apartment
  case Bungalow
  case Detached
  case Duplex
  case EndOfTerrace
  case House
  case SemiDetached
  case Site
  case Terraced
}

object PropertyType {

  import PropertyType.*

  def tryFromString(value: String): Try[PropertyType] = value match {
    case "Apartment"                  => Success(Apartment)
    case "Bungalow"                   => Success(Bungalow)
    case "Detached Bungalow and Land" => Success(Bungalow)
    case "Detached"                   => Success(Detached)
    case "Detached House"             => Success(Detached)
    case "Detached House and Land"    => Success(Detached)
    case "Duplex"                     => Success(Duplex)
    case "End of Terrace"             => Success(EndOfTerrace)
    case "End-terrace House"          => Success(EndOfTerrace)
    case "House and Land"             => Success(House)
    case "Semi-D"                     => Success(SemiDetached)
    case "Semi-detached House"        => Success(SemiDetached)
    case "Site"                       => Success(Site)
    case "Terrace"                    => Success(Terraced)
    case "Mid-terrace House"          => Success(Terraced)
    case unknown                      => Failure(Exception(s"Unknown propertyType: $unknown"))
  }
  given JsonCodec[PropertyType] = DeriveJsonCodec.gen[PropertyType]
}
