package ie.nok.adverts.dooglasNewmanGood

import zio.json.{DeriveJsonDecoder, JsonDecoder}
import zio.http.Client
import zio.ZIO

object Negotiators {
  case class Response(data: ResponseData)
  given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  case class ResponseData(teams: List[ResponseDataTeam])
  given JsonDecoder[ResponseData] = DeriveJsonDecoder.gen[ResponseData]

  case class ResponseDataTeam(
      Name: String,
      Email: String,
      Phone: Option[String],
      Mobile_No: Option[String]
  )
  given JsonDecoder[ResponseDataTeam] = DeriveJsonDecoder.gen[ResponseDataTeam]

  def getByEmail(
      email: String
  ): ZIO[Client, Throwable, Option[ResponseDataTeam]] = {
    val query =
      s"""{ "query": "query { teams ( where: { Email: \\"$email\\" }, limit: 1) { Name, Email, Phone } }"}"""
    GraphQl
      .query[Response](query)
      .map { _.data.teams.headOption }
  }

}
