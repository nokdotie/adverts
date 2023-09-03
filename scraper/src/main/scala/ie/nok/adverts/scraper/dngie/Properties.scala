package ie.nok.adverts.scraper.dngie

import ie.nok.ber.Rating
import ie.nok.adverts._
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.json.ast.Json
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.stream.ZStream
import zio.Schedule.{recurs, fixed}

object Properties {

  private case class Response(data: ResponseData)
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponseData(
      properties: List[Option[ResponseDataProperty]]
  )
  private given JsonDecoder[ResponseData] = DeriveJsonDecoder.gen[ResponseData]

  private case class ResponseDataProperty(
      bathroom: Option[Int],
      bedroom: Option[Int],
      display_address: String,
      floorarea_min: Float,
      floorarea_type: Option[String],
      images: Option[List[ResponseDataPropertyImage]],
      price: Option[Int],
      property_url: String,
      latitude: BigDecimal,
      longitude: BigDecimal,
      extras: Option[ResponseDataPropertyExtras]
  )
  private given JsonDecoder[ResponseDataProperty] =
    DeriveJsonDecoder.gen[ResponseDataProperty]

  private case class ResponseDataPropertyImage(
      url: Option[String],
      srcUrl: Option[String],
      order: Option[Int]
  )
  private given JsonDecoder[ResponseDataPropertyImage] =
    DeriveJsonDecoder.gen[ResponseDataPropertyImage]

  private case class ResponseDataPropertyExtras(
      extrasField: Option[ResponseDataPropertyExtrasField]
  )
  private given JsonDecoder[ResponseDataPropertyExtras] =
    DeriveJsonDecoder.gen[ResponseDataPropertyExtras]

  private case class ResponseDataPropertyExtrasField(
      pBERNumber: Option[Json],
      pBERRating: Option[String],
      pEPI: Option[Json]
  )
  private given JsonDecoder[ResponseDataPropertyExtrasField] =
    DeriveJsonDecoder.gen[ResponseDataPropertyExtrasField]

  private def getRequestQuery(start: Int, limit: Int): String =
    s"""{ "query": "query {
      properties (
        where: {
          status: \\"For Sale\\",
          publish: true
        },
        start: $start,
        limit: $limit
      ) {
        bathroom
        bedroom
        display_address
        floorarea_min
        floorarea_type
        images
        price
        property_url
        latitude
        longitude
        extras
      }
    }"}""".replaceAll("\n", " ")

  private val requestUrl = "https://dng-strapi.q.starberry.com/graphql"
  private val requestAuthorizationHeader = Headers(
    "authorization",
    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYxNjNlMjJkODA2NjIyMGQ0ZGM5N2Q0NCIsImlhdCI6MTYzMzkzNjQ2MywiZXhwIjoxOTQ5NTEyNDYzfQ.f8Wr84jf6zocohajraKkkca-STteDDZUrmZvTrQ479Y"
  )
  private val requestContentTypeHeader =
    Headers("content-type", "application/json")

  private def getResponse(query: String): ZIO[ZioClient, Throwable, Response] =
    Client
      .requestBodyAsJson[Response](
        requestUrl,
        Method.POST,
        requestAuthorizationHeader ++ requestContentTypeHeader,
        Body.fromString(query)
      )
      .retry(recurs(3) && fixed(1.second))

  private def toAdvert(
      property: ResponseDataProperty
  ): Advert = {
    val coordinates = Coordinates(
      latitude = property.latitude,
      longitude = property.longitude
    )

    val imageUrls =
      property.images.getOrElse(List.empty).sortBy { _.order }.flatMap {
        image => image.url.orElse(image.srcUrl)
      }

    val sizeUnit = property.floorarea_type
      .flatMap {
        case "squareMetres" => Option(AreaUnit.SquareMetres)
        case "squareFeet"   => Option(AreaUnit.SquareFeet)
        case "Acres"        => Option(AreaUnit.Acres)
        case "Hectares"     => Option(AreaUnit.Hectares)
        case "" | "3" | "4" => Option.empty
        case other =>
          throw new Exception(
            s"Unknown floorarea_type: $other, ${property.property_url}"
          )
      }

    val size = sizeUnit.map { unit =>
      Area(BigDecimal(property.floorarea_min), unit)
    }

    val sizeInSqtMtr = size.map { Area.toSquareMetres(_).value }

    Advert(
      advertUrl = property.property_url,
      advertPriceInEur = property.price.getOrElse(0),
      propertyAddress = property.display_address,
      propertyCoordinates = coordinates,
      propertyImageUrls = imageUrls,
      propertySize = size.getOrElse(Area.zero),
      propertySizeInSqtMtr = sizeInSqtMtr.getOrElse(0),
      propertyBedroomsCount = property.bedroom.getOrElse(0),
      propertyBathroomsCount = property.bathroom.getOrElse(0),
      createdAt = Instant.now()
    )
  }

  val stream: ZStream[ZioClient, Throwable, Advert] =
    val limit = 25

    ZStream
      .iterate(0)(_ + limit)
      .map { start => getRequestQuery(start, limit) }
      .mapZIOParUnordered(5) { getResponse }
      .map { _.data.properties.flatten }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { toAdvert }
}
