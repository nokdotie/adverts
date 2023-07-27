package ie.nok.adverts.scraper.propertypalcom

import ie.nok.adverts.Advert
import ie.nok.http.Client
import ie.nok.geographic.{Coordinates, GeoHash}
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
      keyInfo: List[ResponsePagePropsPropertyKeyInfo],
      shareURL: String,
      coordinate: ResponsePagePropsPropertyCoordinate
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
      latitude: BigDecimal,
      longitude: BigDecimal
  )
  protected[propertypalcom] given JsonDecoder[
    ResponsePagePropsPropertyCoordinate
  ] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyCoordinate]

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

  protected[propertypalcom] def toAdvert(
      property: ResponsePagePropsProperty
  ): Advert = {
    val price = property.keyInfo
      .find { _.key == "PRICE" }
      .flatMap { _.text }
      .flatMap { _.filter(_.isDigit).toIntOption }
      .getOrElse(0)

    val size = property.keyInfo
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
      .getOrElse(Area.empty)

    val bedroomsCount = property.keyInfo
      .find { _.key == "BEDROOMS" }
      .flatMap { _.text }
      .flatMap { _.filter(_.isDigit).toIntOption }
      .getOrElse(0)

    val bathroomsCount = property.keyInfo
      .find { _.key == "BATHROOMS" }
      .flatMap { _.text }
      .flatMap { _.filter(_.isDigit).toIntOption }
      .getOrElse(0)

    val coordinates = Coordinates(
      latitude = property.coordinate.latitude,
      longitude = property.coordinate.longitude
    )

    Advert(
      advertUrl = property.shareURL,
      advertPriceInEur = price,
      propertyAddress = property.displayAddress,
      propertyCoordinates = coordinates,
      propertyGeoHash = GeoHash.fromCoordinates(coordinates),
      propertyImageUrls = property.images.getOrElse(List.empty).map(_.url),
      propertySize = size,
      propertySizeInSqtMtr = Area.toSquareMetres(size).value,
      propertyBedroomsCount = bedroomsCount,
      propertyBathroomsCount = bathroomsCount,
      createdAt = Instant.now
    )
  }

  def pipeline(
      buildId: String
  ): ZPipeline[ZioClient, Throwable, PropertyIdAndAddress, Advert] =
    ZPipeline
      .map { getApiRequestUrl(buildId, _) }
      .mapZIOParUnordered(5) { getApiResponse }
      .map { _.pageProps.property }
      .map { toAdvert }

}
