package ie.nok.adverts.sherryFitzGerald

import ie.nok.adverts.utils.zio.Client
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.http.model.Headers
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}

object Properties {
  private case class Response(features: List[ResponseFeature])
  private given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  private case class ResponseFeature(properties: ResponseFeatureProperty)
  private given JsonDecoder[ResponseFeature] =
    DeriveJsonDecoder.gen[ResponseFeature]

  private case class ResponseFeatureProperty(link: String)
  private given JsonDecoder[ResponseFeatureProperty] =
    DeriveJsonDecoder.gen[ResponseFeatureProperty]

  private def getRequestUrl(page: Int): String =
    s"https://www.sherryfitz.ie/sfdev/api/properties/?type=all&sort=created_at&order=desc&page=$page"

  private def getResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] = {
    val refererHeader = Headers("Referer", "https://www.sherryfitz.ie/")

    Client
      .requestJson[Response](url, headers = refererHeader)
      .retry(recurs(3) && fixed(1.second))
  }

  val stream: ZStream[ZioClient, Throwable, String] =
    ZStream
      .iterate(1)(_ + 1)
      .map { getRequestUrl }
      .mapZIOParUnordered(5) { getResponse }
      .map { _.features }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { _.properties.link }

}
