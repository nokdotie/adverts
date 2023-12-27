package ie.nok.adverts.scraper.daftie

import ie.nok.http.Client
import java.time.Instant
import java.util.UUID
import scala.util.Try
import zio.Schedule.{fixed, recurs}
import zio.http.model.{Headers, Method}
import zio.http.{Body, Client as ZioClient}
import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.stream.ZStream
import zio.{ZIO, durationInt}

object Properties {
  protected[daftie] case class Response(listings: List[ResponseListing])
  protected[daftie] given JsonDecoder[Response] =
    DeriveJsonDecoder.gen[Response]

  protected[daftie] case class ResponseListing(listing: ResponseListingListing)
  protected[daftie] given JsonDecoder[ResponseListing] =
    DeriveJsonDecoder.gen[ResponseListing]

  protected[daftie] case class ResponseListingListing(id: Int)
  protected[daftie] given JsonDecoder[ResponseListingListing] =
    DeriveJsonDecoder.gen[ResponseListingListing]

  private val streamApiRequestContent: ZStream[Any, Nothing, String] = {
    val pageSize = 100
    ZStream
      .iterate(0)(_ + pageSize)
      .map { from =>
        s"""{"section":"residential-for-sale","filters":[{"name":"adState","values":["published"]}],"andFilters":[],"ranges":[],"paging":{"from":"$from","pageSize":"$pageSize"},"geoFilter":{},"terms":""}"""
      }
  }

  private def getApiResponse(
      content: String
  ): ZIO[ZioClient, Throwable, Response] = {
    val brandHeader       = Headers("brand", "daft")
    val platformHeader    = Headers("platform", "web")
    val contentTypeHeader = Headers("content-type", "application/json")

    Client
      .requestBodyAsJson[Response](
        "https://gateway.daft.ie/old/v1/listings",
        method = Method.POST,
        headers = brandHeader ++ platformHeader ++ contentTypeHeader,
        content = Body.fromString(content)
      )
      .retry(recurs(3) && fixed(1.second))
  }

  val stream: ZStream[ZioClient, Throwable, Int] =
    streamApiRequestContent
      .mapZIOPar(5) { getApiResponse }
      .map { _.listings }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { _.listing.id }

}
