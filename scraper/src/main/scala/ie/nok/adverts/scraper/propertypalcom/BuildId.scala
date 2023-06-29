package ie.nok.adverts.scraper.propertypalcom

import ie.nok.http.Client
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}

object BuildId {

  private val buildIdRegex = """"buildId":"([^"]+)"""".r

  val latest: ZIO[ZioClient, Throwable, String] =
    Client
      .requestBody("https://www.propertypal.com/")
      .retry(recurs(3) && fixed(1.second))
      .map { buildIdRegex.findFirstMatchIn(_).map { _.group(1) } }
      .someOrFail { new Exception("Failed to find buildId") }

}
