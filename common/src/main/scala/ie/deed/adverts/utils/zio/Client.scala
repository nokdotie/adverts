package ie.nok.adverts.utils.zio

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.http.{Body, Client => ZioClient}
import zio.json.{DecoderOps, JsonDecoder}
import zio.http.model.{Method, Headers}

object Client {

  def requestJson[A: JsonDecoder](
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient, Throwable, A] =
    ZioClient
      .request(url, method, headers, content)
      .flatMap { _.body.asString }
      .flatMap {
        _.fromJson[A].left
          .map(Throwable(_))
          .pipe(ZIO.fromEither)
      }

  def requestHtml(
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient, Throwable, Document] =
    ZioClient
      .request(url, method, headers, content)
      .flatMap { _.body.asString }
      .flatMap { html => ZIO.attempt { Jsoup.parse(html) } }
}
