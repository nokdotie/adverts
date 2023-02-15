package ie.deed.websites

import _root_.ie.deed.{Scraper, Record}
import scala.util.chaining.scalaUtilChainingOps
import zio.stream.ZStream
import zio.json._
import zio._
import zio.http.model.Headers
import zio.http.{Client, ZClient, Body, Response}
import java.time.Instant
import zio.http.model.Method

object DngIe extends Scraper:
  case class PropertiesApiResponse(data: PropertiesApiResponseData)
  object PropertiesApiResponse {
    implicit val decoder: JsonDecoder[PropertiesApiResponse] =
      DeriveJsonDecoder.gen[PropertiesApiResponse]
  }
  case class PropertiesApiResponseData(
      properties: List[PropertiesApiResponseDataProperty]
  )
  object PropertiesApiResponseData {
    implicit val decoder: JsonDecoder[PropertiesApiResponseData] =
      DeriveJsonDecoder.gen[PropertiesApiResponseData]
  }
  case class PropertiesApiResponseDataProperty(
      id: String,
      price: Option[Int],
      address: PropertiesApiResponseDataPropertyAddress,
      images: Option[List[PropertiesApiResponseDataPropertyImage]],
      crm_negotiator_id: Option[PropertiesApiResponseDataPropertyNegotiator]
  )
  object PropertiesApiResponseDataProperty {
    implicit val decoder: JsonDecoder[PropertiesApiResponseDataProperty] =
      DeriveJsonDecoder.gen[PropertiesApiResponseDataProperty]
  }
  case class PropertiesApiResponseDataPropertyAddress(
      postcode: Option[String]
  )
  object PropertiesApiResponseDataPropertyAddress {
    implicit val decoder
        : JsonDecoder[PropertiesApiResponseDataPropertyAddress] =
      DeriveJsonDecoder.gen[PropertiesApiResponseDataPropertyAddress]
  }
  case class PropertiesApiResponseDataPropertyImage(
      url: Option[String],
      srcUrl: Option[String],
      order: Option[Int]
  )
  object PropertiesApiResponseDataPropertyImage {
    implicit val decoder: JsonDecoder[PropertiesApiResponseDataPropertyImage] =
      DeriveJsonDecoder.gen[PropertiesApiResponseDataPropertyImage]
  }
  case class PropertiesApiResponseDataPropertyNegotiator(
      Email: Option[String]
  )
  object PropertiesApiResponseDataPropertyNegotiator {
    implicit val decoder
        : JsonDecoder[PropertiesApiResponseDataPropertyNegotiator] =
      DeriveJsonDecoder.gen[PropertiesApiResponseDataPropertyNegotiator]
  }

  case class NegotiatorApiResponse(data: NegotiatorApiResponseData)
  object NegotiatorApiResponse {
    implicit val decoder: JsonDecoder[NegotiatorApiResponse] =
      DeriveJsonDecoder.gen[NegotiatorApiResponse]
  }
  case class NegotiatorApiResponseData(
      teams: List[NegotiatorApiResponseDataTeam]
  )
  object NegotiatorApiResponseData {
    implicit val decoder: JsonDecoder[NegotiatorApiResponseData] =
      DeriveJsonDecoder.gen[NegotiatorApiResponseData]
  }
  case class NegotiatorApiResponseDataTeam(
      Name: String,
      Email: String,
      Phone: Option[String],
      Mobile_No: Option[String]
  )
  object NegotiatorApiResponseDataTeam {
    implicit val decoder: JsonDecoder[NegotiatorApiResponseDataTeam] =
      DeriveJsonDecoder.gen[NegotiatorApiResponseDataTeam]
  }

  val apiUrl = "https://dng-strapi.q.starberry.com/graphql"
  val apiAuthorizationHeader = Headers(
    "authorization",
    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYxNjNlMjJkODA2NjIyMGQ0ZGM5N2Q0NCIsImlhdCI6MTYzMzkzNjQ2MywiZXhwIjoxOTQ5NTEyNDYzfQ.f8Wr84jf6zocohajraKkkca-STteDDZUrmZvTrQ479Y"
  )
  val apiContentTypeHeader = Headers("content-type", "application/json")

  private val getPropertiesApiQueries: ZStream[Any, Nothing, String] =
    val limit = 25
    ZStream
      .iterate(0)(_ + limit)
      .map { start =>
        s"""{ "query": "query { properties ( where: { status: \\"For Sale\\", publish: true }, start: $start, limit: $limit) { id, price, address, images, crm_negotiator_id } }"}"""
      }

  private def getPropertiesFromApi(
      query: String
  ): ZIO[Client, Throwable, PropertiesApiResponse] =
    Client
      .request(
        apiUrl,
        Method.POST,
        headers = apiAuthorizationHeader ++ apiContentTypeHeader,
        content = Body.fromString(query)
      )
      .retryN(3)
      .flatMap { _.body.asString }
      .flatMap {
        _.fromJson[PropertiesApiResponse].left
          .map(Throwable(_))
          .pipe(ZIO.fromEither)
      }

  private def getNegotiatorApiQuery(email: String): String =
    s"""{ "query": "query { teams ( where: { Email: \\"$email\\" }, limit: 1) { Name, Email, Phone } }"}"""

  private def getNegotiatorFromApi(
      query: String
  ): ZIO[Client, Throwable, NegotiatorApiResponse] =
    Client
      .request(
        apiUrl,
        Method.POST,
        headers = apiAuthorizationHeader ++ apiContentTypeHeader,
        content = Body.fromString(query)
      )
      .retryN(3)
      .flatMap { _.body.asString }
      .flatMap {
        _.fromJson[NegotiatorApiResponse].left
          .map(Throwable(_))
          .pipe(ZIO.fromEither)
      }

  private def buildRecord(
      property: PropertiesApiResponseDataProperty,
      negotiator: NegotiatorApiResponseDataTeam
  ): Option[Record] =
    (
      property.price,
      property.address.postcode,
      negotiator.Phone.orElse(negotiator.Mobile_No)
    )
      .pipe {
        case (Some(price), Some(postcode), Some(phone)) =>
          Some((price, postcode, phone))
        case _ => None
      }
      .map { (price, postcode, phone) =>
        Record(
          at = Instant.now(),
          advertUrl = s"https://www.dng.ie/property-for-sale/-${property.id}",
          advertPrice = price,
          propertyEircode = postcode,
          propertyImageUrls =
            property.images.getOrElse(List.empty).sortBy { _.order }.flatMap {
              image => image.url.orElse(image.srcUrl)
            },
          contactName = negotiator.Name,
          contactPhone = phone,
          contactEmail = negotiator.Email
        )
      }

  val scrape = getPropertiesApiQueries
    .mapZIOPar(5) { getPropertiesFromApi }
    .map { _.data.properties }
    .takeWhile { _.nonEmpty }
    .flattenIterables
    .map { property =>
      property.crm_negotiator_id
        .flatMap { _.Email }
        .map { (property, _) }
    }
    .collectSome
    .mapZIOPar(5) { (property, negotiatorEmail) =>
      getNegotiatorApiQuery(negotiatorEmail)
        .pipe { getNegotiatorFromApi }
        .map { _.data.teams.headOption.map { (property, _) } }
    }
    .collectSome
    .map { buildRecord.tupled }
    .collectSome
