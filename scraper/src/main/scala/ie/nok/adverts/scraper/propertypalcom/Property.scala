package ie.nok.adverts.scraper.propertypalcom

import ie.nok.adverts._
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

    val coordinates = Coordinates(
      latitude = property.coordinate.latitude,
      longitude = property.coordinate.longitude
    )

    val imageUrls = property.images.getOrElse(List.empty).map(_.url)

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

    val sizeInSqtMtr = size.map { Area.toSquareMetres(_) }.map { _.value }

    val bedroomsCount = property.keyInfo
      .find { _.key == "BEDROOMS" }
      .flatMap { _.text }
      .flatMap { _.filter(_.isDigit).toIntOption }

    val bathroomsCount = property.keyInfo
      .find { _.key == "BATHROOMS" }
      .flatMap { _.text }
      .flatMap { _.filter(_.isDigit).toIntOption }

    val source = AdvertSource(
      service = AdvertService.PropertyPalCom,
      url = property.shareURL
    )

    val attributes = List(
      AdvertAttribute.Address(property.displayAddress, source),
      AdvertAttribute.Coordinates(coordinates, source)
    ) ++ price.map { AdvertAttribute.PriceInEur(_, source) }
      ++ imageUrls.map { AdvertAttribute.ImageUrl(_, source) }
      ++ sizeInSqtMtr.map { AdvertAttribute.SizeInSqtMtr(_, source) }
      ++ bedroomsCount.map { AdvertAttribute.BedroomsCount(_, source) }
      ++ bathroomsCount.map { AdvertAttribute.BathroomsCount(_, source) }

    Advert(
      advertUrl = property.shareURL,
      advertPriceInEur = price.getOrElse(0),
      propertyAddress = property.displayAddress,
      propertyCoordinates = coordinates,
      propertyImageUrls = imageUrls,
      propertySize = size.getOrElse(Area.empty),
      propertySizeInSqtMtr = sizeInSqtMtr.getOrElse(0),
      propertyBedroomsCount = bedroomsCount.getOrElse(0),
      propertyBathroomsCount = bathroomsCount.getOrElse(0),
      attributes = attributes,
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
