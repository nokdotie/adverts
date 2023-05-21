package ie.deed.adverts.dooglasNewmanGood

import ie.deed.adverts.utils.zio.Client
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Body, Client => ZioClient}
import zio.http.model.{Headers, Method}
import zio.json.{JsonDecoder, DecoderOps}

object GraphQl {
  val url = "https://dng-strapi.q.starberry.com/graphql"
  val authorizationHeader = Headers(
    "authorization",
    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYxNjNlMjJkODA2NjIyMGQ0ZGM5N2Q0NCIsImlhdCI6MTYzMzkzNjQ2MywiZXhwIjoxOTQ5NTEyNDYzfQ.f8Wr84jf6zocohajraKkkca-STteDDZUrmZvTrQ479Y"
  )
  val contentTypeHeader = Headers("content-type", "application/json")

  def query[A: JsonDecoder](query: String): ZIO[ZioClient, Throwable, A] =
    Client
      .requestJson[A](
        url,
        Method.POST,
        authorizationHeader ++ contentTypeHeader,
        Body.fromString(query)
      )
      .retry(recurs(3) && fixed(1.second))
}
