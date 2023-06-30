package ie.nok.adverts.scraper.propertypalcom

import ie.nok.adverts.Advert
import ie.nok.http.Client
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import java.time.Instant
import zio.stream.ZPipeline

object Property {
  protected[propertypalcom] case class Response(pageProps: ResponsePageProps)
  protected[propertypalcom] given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  protected[propertypalcom] case class ResponsePageProps(
      property: ResponsePagePropsProperty
  )
  protected[propertypalcom] given JsonDecoder[ResponsePageProps] =
    DeriveJsonDecoder.gen[ResponsePageProps]

  protected[propertypalcom] case class ResponsePagePropsProperty(
      displayAddress: String,
      images: Option[List[ResponsePagePropsPropertyImage]],
      keyInfo: List[ResponsePagePropsPropertyKeyInfo],
      shareURL: String
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsProperty] =
    DeriveJsonDecoder.gen[ResponsePagePropsProperty]

  protected[propertypalcom] case class ResponsePagePropsPropertyKeyInfo(
      key: String,
      text: Option[String]
  )
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsPropertyKeyInfo] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyKeyInfo]

  protected[propertypalcom] case class ResponsePagePropsPropertyImage(url: String)
  protected[propertypalcom] given JsonDecoder[ResponsePagePropsPropertyImage] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyImage]

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

    val sizeInSqtMtr = property.keyInfo
      .find { _.key == "SIZE" }
      .flatMap { _.text }
      .map { _.replaceFirst(",", "") }
      .flatMap { "([0-9]+\\.?[0-9]*) (sq\\. metres|sq\\. feet|acres)".r.findFirstMatchIn }
      .map { found => (found.group(1), found.group(2)) }
      .map {
        case (value, "sq. metres") => BigDecimal(value)
        case (value, "sq. feet") => BigDecimal(value) * 0.092903
        case (value, "acres") => BigDecimal(value) * 4046.86
        case (value, other) =>
          throw new Exception(
            s"Unknown unit: $other, $value, ${property.shareURL}"
          )
      }
      .getOrElse(BigDecimal(0))

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

    Advert(
      advertUrl = property.shareURL,
      advertPriceInEur = price,
      propertyAddress = property.displayAddress,
      propertyImageUrls = property.images.getOrElse(List.empty).map(_.url),
      propertySizeInSqtMtr = sizeInSqtMtr,
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
