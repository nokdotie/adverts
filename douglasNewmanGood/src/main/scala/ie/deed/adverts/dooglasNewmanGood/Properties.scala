package ie.nok.adverts.dooglasNewmanGood

import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.http.Client
import zio.stream.ZStream

object Properties {

  case class Response(data: ResponseData)
  given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  case class ResponseData(properties: List[ResponseDataProperty])
  given JsonDecoder[ResponseData] = DeriveJsonDecoder.gen[ResponseData]

  case class ResponseDataProperty(
      id: String,
      price: Option[Int],
      address: ResponseDataPropertyAddress,
      images: Option[List[ResponseDataPropertyImage]],
      crm_negotiator_id: Option[ResponseDataPropertyNegotiator]
  )
  given JsonDecoder[ResponseDataProperty] =
    DeriveJsonDecoder.gen[ResponseDataProperty]

  case class ResponseDataPropertyAddress(postcode: Option[String])
  given JsonDecoder[ResponseDataPropertyAddress] =
    DeriveJsonDecoder.gen[ResponseDataPropertyAddress]

  case class ResponseDataPropertyImage(
      url: Option[String],
      srcUrl: Option[String],
      order: Option[Int]
  )
  given JsonDecoder[ResponseDataPropertyImage] =
    DeriveJsonDecoder.gen[ResponseDataPropertyImage]

  case class ResponseDataPropertyNegotiator(Email: Option[String])
  given JsonDecoder[ResponseDataPropertyNegotiator] =
    DeriveJsonDecoder.gen[ResponseDataPropertyNegotiator]

  val stream: ZStream[Client, Throwable, ResponseDataProperty] = {
    val limit = 25

    ZStream
      .iterate(0)(_ + limit)
      .map { start =>
        s"""{ "query": "query { properties ( where: { status: \\"For Sale\\", publish: true }, start: $start, limit: $limit) { id, price, address, images, crm_negotiator_id } }"}"""
      }
      .mapZIOPar(5) { GraphQl.query[Response] }
      .map { _.data.properties }
      .takeWhile { _.nonEmpty }
      .flattenIterables
  }

  def getNegotiatorEmail(property: ResponseDataProperty): Option[String] =
    property.crm_negotiator_id.flatMap { _.Email }
}
