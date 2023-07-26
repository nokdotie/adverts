package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.http.model.Headers
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}

object Properties {
  private case class Response(features: List[ResponseFeature])
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponseFeature(
      geometry: ResponseFeatureGeometry,
      properties: ResponseFeatureProperty
  )
  private given JsonDecoder[ResponseFeature] =
    DeriveJsonDecoder.gen[ResponseFeature]

  private case class ResponseFeatureGeometry(coordinates: List[BigDecimal])
  private given JsonDecoder[ResponseFeatureGeometry] =
    DeriveJsonDecoder.gen[ResponseFeatureGeometry]

  private case class ResponseFeatureProperty(link: String)
  private given JsonDecoder[ResponseFeatureProperty] =
    DeriveJsonDecoder.gen[ResponseFeatureProperty]

  private val requestUrl: String =
    s"https://sherryfitz.ie/sfdev/api/properties/?type=all&fields=map"

  private def getResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] = {
    val refererHeader = Headers("Referer", "https://sherryfitz.ie/")

    Client
      .requestBodyAsJson[Response](url, headers = refererHeader)
      .retry(recurs(3) && fixed(1.second))
  }

  val stream: ZStream[ZioClient, Throwable, (String, Coordinates)] =
    requestUrl
      .pipe { getResponse }
      .map { _.features }
      .pipe { ZStream.fromIterableZIO }
      .map { feature =>
        (
          feature.properties.link,
          Coordinates(
            latitude = feature.geometry.coordinates(1),
            longitude = feature.geometry.coordinates(0)
          )
        )
      }

}
