package ie.nok.adverts.scraper.myhomeie

import ie.nok.http.Client
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}

object ApiKey {

  private val regex = """ApiKey=([^&]+)""".r

  val latest: ZIO[ZioClient, Throwable, String] =
    Client
      .requestBody("https://www.myhome.ie")
      .retry(recurs(3) && fixed(1.second))
      .map { regex.findFirstMatchIn(_).map { _.group(1) } }
      .someOrFail { new Exception("Failed to find ApiKey") }

}
