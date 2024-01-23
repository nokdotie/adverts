package ie.nok.adverts

import zio.json.{DeriveJsonCodec, JsonCodec}

import scala.util.{Failure, Success, Try}

enum PropertyType {
  case Apartment
  case Bungalow
  case Detached
  case Duplex
  case EndOfTerrace
  case SemiDetached
  case Site
  case Terraced
}

object PropertyType {

  import PropertyType.*

  def tryFromString(value: String): Try[PropertyType] = value match {
    case "Apartment"      => Success(Apartment)
    case "Bungalow"       => Success(Bungalow)
    case "Detached"       => Success(Detached)
    case "Duplex"         => Success(Duplex)
    case "End of Terrace" => Success(EndOfTerrace)
    case "Semi-D"         => Success(SemiDetached)
    case "Site"           => Success(Site)
    case "Terrace"        => Success(Terraced)
    case unknown          => Failure(Exception(s"Unknown rating: $unknown"))
  }
  given JsonCodec[PropertyType] = DeriveJsonCodec.gen[PropertyType]
}
