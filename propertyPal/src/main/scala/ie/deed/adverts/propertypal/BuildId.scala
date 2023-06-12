package ie.nok.adverts.propertypal

import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}

object BuildId {

  private val buildIdRegex = """"buildId":"([^"]+)"""".r

  val latest: ZIO[ZioClient, Throwable, String] =
    ZioClient.request("https://www.propertypal.com/")
        .retry(recurs(3) && fixed(1.second))
        .flatMap { _.body.asString }
        .map { buildIdRegex.findFirstMatchIn(_).map { _.group(1) } }
        .someOrFail { new Exception("Failed to find buildId") }

}
