package ie.nok.adverts.scraper.myhomeie

import ie.nok.adverts.Advert
import ie.nok.adverts.services.myhomeie.MyHomeIeAdvert
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.http.model.Headers
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import zio.stream.ZPipeline

object Property {
  private case class Response(Brochure: ResponseBrochure)
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponseBrochure(
      Property: ResponseBrochureProperty
  )
  private given JsonDecoder[ResponseBrochure] = DeriveJsonDecoder.gen[ResponseBrochure]

  private case class ResponseBrochureProperty(
      PropertyId: Int,
      DisplayAddress: String,
      Eircode: String,
      Photos: List[String],
      BathString: Option[String],
      BedsString: Option[String],
      PriceAsString: String,
      SizeStringMeters: Option[BigDecimal],
      BerRating: Option[String],
      BrochureContent: List[ResponseBrochurePropertyBrochureContent],
      BrochureMap: Option[ResponseBrochurePropertyBrochureMap]
  )
  private given JsonDecoder[ResponseBrochureProperty] = DeriveJsonDecoder.gen[ResponseBrochureProperty]

  private case class ResponseBrochurePropertyBrochureContent(
      ContentType: String,
      Content: String
  )
  private given JsonDecoder[ResponseBrochurePropertyBrochureContent] = DeriveJsonDecoder.gen[ResponseBrochurePropertyBrochureContent]

  private case class ResponseBrochurePropertyBrochureMap(
      longitude: BigDecimal,
      latitude: BigDecimal
  )
  private given JsonDecoder[ResponseBrochurePropertyBrochureMap] = DeriveJsonDecoder.gen[ResponseBrochurePropertyBrochureMap]

  private def getRequestUrl(apiKey: String, propertyId: Int): String =
    s"https://api.myhome.ie/brochure/$propertyId?ApiKey=$apiKey&format=json"

  private def getApiResponse(url: String): ZIO[ZioClient, Throwable, Response] = {
    val contentTypeHeader = Headers("content-type", "application/json")

    Client
      .requestBodyAsJson[Response](
        url,
        headers = contentTypeHeader
      )
      .retry(recurs(3) && fixed(1.second))
  }

  private def toMyHomeIeAdvert(response: Response): MyHomeIeAdvert = {
    val url = s"https://www.myhome.ie/${response.Brochure.Property.PropertyId}"
    val price = response.Brochure.Property.PriceAsString
      //   .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val description = response.Brochure.Property.BrochureContent
      .find(_.ContentType == "Description")
      .map(_.Content)

    val coordinates = response.Brochure.Property.BrochureMap.map { bm =>
      Coordinates(
        latitude = bm.latitude,
        longitude = bm.longitude
      )
    }

    val size = response.Brochure.Property.SizeStringMeters
      .map { Area(_, AreaUnit.SquareMetres) }

    val bedroomsCount = response.Brochure.Property.BedsString
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val bathroomsCount = response.Brochure.Property.BathString
      .getOrElse("")
      .filter(_.isDigit)
      .toIntOption

    val buildingEnergyRating = response.Brochure.Property.BerRating
      .flatMap { Rating.tryFromString(_).toOption }

    MyHomeIeAdvert(
      url = url,
      priceInEur = price,
      description = description,
      address = response.Brochure.Property.DisplayAddress,
      coordinates = coordinates,
      imageUrls = response.Brochure.Property.Photos,
      size = size,
      bedroomsCount = bedroomsCount,
      bathroomsCount = bathroomsCount,
      buildingEnergyRating = buildingEnergyRating,
      createdAt = Instant.now
    )
  }

  def pipeline(apiKey: String): ZPipeline[ZioClient, Throwable, Int, Advert] =
    ZPipeline
      .map { getRequestUrl(apiKey, _) }
      .mapZIOParUnordered(5) {
        getApiResponse(_)
          .fold(
            (throwable) => {
              println(s"Failure: ${throwable.getMessage()}")
              throwable.printStackTrace()

              None
            },
            Option.apply(_)
          )
      }
      .collectSome
      .map { toMyHomeIeAdvert }
      .map { MyHomeIeAdvert.toAdvert }

}
