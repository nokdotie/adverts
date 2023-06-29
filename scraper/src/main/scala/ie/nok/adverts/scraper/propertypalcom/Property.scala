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
  private case class Response(pageProps: ResponsePageProps)
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponsePageProps(
      property: ResponsePagePropsProperty
  )
  private given JsonDecoder[ResponsePageProps] =
    DeriveJsonDecoder.gen[ResponsePageProps]

  private case class ResponsePagePropsProperty(
      displayAddress: String,
      images: List[ResponsePagePropsPropertyImage],
      keyInfo: List[ResponsePagePropsPropertyKeyInfo],
      shareURL: String
  )
  private given JsonDecoder[ResponsePagePropsProperty] =
    DeriveJsonDecoder.gen[ResponsePagePropsProperty]

  private case class ResponsePagePropsPropertyKeyInfo(
      key: String,
      text: Option[String]
  )
  private given JsonDecoder[ResponsePagePropsPropertyKeyInfo] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyKeyInfo]

  private case class ResponsePagePropsPropertyImage(url: String)
  private given JsonDecoder[ResponsePagePropsPropertyImage] =
    DeriveJsonDecoder.gen[ResponsePagePropsPropertyImage]

  private def getApiRequestUrl(
      buildId: String,
      propertyIdAndAddress: PropertyIdAndAddress
  ): String =
    s"https://www.propertypal.com/_next/data/$buildId/en/property.json?address=${propertyIdAndAddress.address}&id=${propertyIdAndAddress.id}"

  private def getApiResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] =
    Client
      .requestBodyAsJson(url)
      .retry(recurs(3) && fixed(1.second))

  private def toAdvert(
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
      .flatMap { "[0-9]+\\.?[0-9]*".r.findFirstIn }
      .fold(BigDecimal(0)) { BigDecimal.apply }

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
      propertyImageUrls = property.images.map(_.url),
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
