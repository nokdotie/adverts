package ie.nok.adverts

import ie.nok.geographic.{Coordinates => NCoordinates}
import ie.nok.geographic.geojson.Feature
import ie.nok.unit.Area
import java.time.Instant
import zio.json.{JsonCodec, DeriveJsonCodec}

enum AdvertAttribute {
  case PriceInEur(value: Int, source: AdvertSource)
  case Address(value: String, source: AdvertSource)
  case Coordinates(value: NCoordinates, source: AdvertSource)
  case ImageUrl(value: String, source: AdvertSource)
  case SizeInSqtMtr(value: BigDecimal, source: AdvertSource)
  case BedroomsCount(value: Int, source: AdvertSource)
  case BathroomsCount(value: Int, source: AdvertSource)
  case BuildingEnergyRating(value: String, source: AdvertSource)
  case BuildingEnergyRatingCertificateNumber(value: Int, source: AdvertSource)
  case BuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(
      value: Float,
      source: AdvertSource
  )
}

object AdvertAttribute {
  given JsonCodec[AdvertAttribute] = DeriveJsonCodec.gen[AdvertAttribute]
}
