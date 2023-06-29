package ie.nok.adverts.scraper.dngie

import ie.nok.adverts.Advert
import ie.nok.http.Client
import java.time.Instant
import zio.{durationInt, ZIO}
import zio.json.{DeriveJsonDecoder, JsonDecoder}
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
      property_url: String
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
    val sizeInSqtMtr = property.floorarea_type
      .map {
        case "squareMetres" => BigDecimal(property.floorarea_min)
        case "squareFeet"   => BigDecimal(property.floorarea_min) * 0.092903
        case "Acres"        => BigDecimal(property.floorarea_min) * 4046.86
        case "Hectares"     => BigDecimal(property.floorarea_min) * 10000
        case "" | "3" | "4" => BigDecimal(0)
        case other =>
          throw new Exception(
            s"Unknown floorarea_type: $other, ${property.floorarea_min}, ${property.property_url}"
          )
      }
      .getOrElse(BigDecimal(0))

    Advert(
      advertUrl = property.property_url,
      advertPriceInEur = property.price.getOrElse(0),
      propertyAddress = property.display_address,
      propertyImageUrls =
        property.images.getOrElse(List.empty).sortBy { _.order }.flatMap {
          image => image.url.orElse(image.srcUrl)
        },
      propertySizeInSqtMtr = sizeInSqtMtr,
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
