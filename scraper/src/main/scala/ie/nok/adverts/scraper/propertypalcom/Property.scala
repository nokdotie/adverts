package ie.nok.adverts.scraper.propertypalcom

import ie.nok.adverts.Advert
import ie.nok.adverts.services.propertypalcom.PropertyPalComAdvert
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import zio.stream.ZPipeline

object Property {
  protected[propertypalcom] case class Response(pageProps: ResponsePageProps)
  protected[propertypalcom] given JsonDecoder[Response] =
    DeriveJsonDecoder.gen[Response]

  protected[propertypalcom] case class ResponsePageProps(
      property: ResponsePagePropsProperty
  )
  protected[propertypalcom] given JsonDecoder[ResponsePageProps] =
    DeriveJsonDecoder.gen[ResponsePageProps]

  protected[propertypalcom] case class ResponsePagePropsProperty(
      displayAddress: String,
      images: Option[List[ResponsePagePropsPropertyImage]],
      keyInfo: Option[List[ResponsePagePropsPropertyKeyInfo]],
      shareURL: Option[String],
      coordinate: Option[ResponsePagePropsPropertyCoordinate],
      ber: Option[ResponsePagePropsPropertyBer]
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsProperty] =
    DeriveJsonDecoder.gen[ResponsePagePropsProperty]

  protected[propertypalcom] case class ResponsePagePropsPropertyKeyInfo(
      key: String,
      text: Option[String]
  )
  protected[propertypalcom] given JsonDecoder[
    ResponsePagePropsPropertyKeyInfo
  ] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyKeyInfo]

  protected[propertypalcom] case class ResponsePagePropsPropertyImage(
      url: String
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsPropertyImage] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyImage]

  protected[propertypalcom] case class ResponsePagePropsPropertyCoordinate(
      latitude: Option[BigDecimal],
      longitude: Option[BigDecimal]
  )
  protected[propertypalcom] given JsonDecoder[
    ResponsePagePropsPropertyCoordinate
  ] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyCoordinate]

  protected[propertypalcom] case class ResponsePagePropsPropertyBer(
      alphanumericRating: Option[String],
      energyPerformanceIndicator: Option[BigDecimal]
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsPropertyBer] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyBer]

  protected[propertypalcom] def getApiRequestUrl(
      buildId: String,
      propertyIdAndAddress: PropertyIdAndAddress
  ): String =
    s"https://www.propertypal.com/_next/data/$buildId/en/property.json?address=${propertyIdAndAddress.address}&id=${propertyIdAndAddress.id}"

  protected[propertypalcom] def getApiResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] =
    Client
      .requestBodyAsJson(url)
      .retry(recurs(3) && fixed(1.second))

  protected[propertypalcom] def keyInfoTextToIntOption(
      property: ResponsePagePropsProperty,
      key: String
  ): Option[Int] =
    property.keyInfo
      .getOrElse(List.empty)
      .find { _.key == key }
      .flatMap { _.text }
      .flatMap { _.filter(_.isDigit).toIntOption }

  protected[propertypalcom] def size(
      property: ResponsePagePropsProperty
  ): Option[Area] =
    property.keyInfo
      .getOrElse(List.empty)
      .find { _.key == "SIZE" }
      .flatMap { _.text }
      .map { _.replaceFirst(",", "") }
      .flatMap {
        "([0-9]+\\.?[0-9]*) (sq\\. metres|sq\\. feet|acres)".r.findFirstMatchIn
      }
      .map { found => (found.group(1), found.group(2)) }
      .map {
        case (value, "sq. metres") =>
          Area(BigDecimal(value), AreaUnit.SquareMetres)
        case (value, "sq. feet") => Area(BigDecimal(value), AreaUnit.SquareFeet)
        case (value, "acres")    => Area(BigDecimal(value), AreaUnit.Acres)
        case (value, other) =>
          throw new Exception(
            s"Unknown unit: $other, ${property.shareURL}"
          )
      }

  protected[propertypalcom] def ber(
      property: ResponsePagePropsProperty
  ): (Option[Rating], Option[BigDecimal]) =
    property.ber
      .fold((None, None)) { ber =>
        (
          ber.alphanumericRating.flatMap { Rating.tryFromString(_).toOption },
          ber.energyPerformanceIndicator
        )
      }

  protected[propertypalcom] def toPropertyPalComAdvertOption(
      property: ResponsePagePropsProperty
  ): Option[PropertyPalComAdvert] = {
    val price = keyInfoTextToIntOption(property, "PRICE")

    val coordinates = property.coordinate
      .flatMap { coordinate =>
        coordinate.latitude.zip(coordinate.longitude)
      }
      .map { (latitude, longitude) =>
        Coordinates(
          latitude = latitude,
          longitude = longitude
        )
      }

    val imageUrls = property.images.getOrElse(List.empty).map(_.url)

    val bedroomsCount  = keyInfoTextToIntOption(property, "BEDROOMS")
    val bathroomsCount = keyInfoTextToIntOption(property, "BATHROOMS")

    val (rating, energyRatingInKWhPerSqtMtrPerYear) = ber(property)

    val (address, eircode) = Eircode.unzip(property.displayAddress)

    property.shareURL.map { url =>
      PropertyPalComAdvert(
        url = url,
        priceInEur = price,
        address = address,
        eircode = eircode,
        coordinates = coordinates,
        imageUrls = imageUrls,
        size = size(property),
        bedroomsCount = bedroomsCount,
        bathroomsCount = bathroomsCount,
        buildingEnergyRating = rating,
        buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = energyRatingInKWhPerSqtMtrPerYear,
        createdAt = Instant.now
      )
    }
  }

  def pipeline(
      buildId: String
  ): ZPipeline[ZioClient, Throwable, PropertyIdAndAddress, Advert] =
    ZPipeline
      .map { getApiRequestUrl(buildId, _) }
      .mapZIOParUnordered(5) { getApiResponse }
      .map { _.pageProps.property }
      .map { toPropertyPalComAdvertOption }
      .collectSome
      .map { PropertyPalComAdvert.toAdvert }

}
