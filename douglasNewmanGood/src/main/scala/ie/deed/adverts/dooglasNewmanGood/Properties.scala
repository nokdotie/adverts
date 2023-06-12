package ie.nok.adverts.dooglasNewmanGood

import ie.nok.adverts.Advert
import java.time.Instant
import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.http.Client
import zio.stream.ZStream

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
      advertPrice = property.price.getOrElse(0),
      propertyAddress = property.display_address,
      propertyImageUrls =
        property.images.getOrElse(List.empty).sortBy { _.order }.flatMap {
          image => image.url.orElse(image.srcUrl)
        },
      propertySizeinSqtMtr = sizeInSqtMtr,
      propertyBedroomsCount = property.bedroom.getOrElse(0),
      propertyBathroomsCount = property.bathroom.getOrElse(0),
      createdAt = Instant.now()
    )
  }

  val stream: ZStream[Client, Throwable, Advert] =
    val limit = 25

    ZStream
      .iterate(0)(_ + limit)
      .map { start =>
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
      }
      .mapZIOParUnordered(5) { GraphQl.query[Response] }
      .map { _.data.properties.flatten }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { toAdvert }
}
