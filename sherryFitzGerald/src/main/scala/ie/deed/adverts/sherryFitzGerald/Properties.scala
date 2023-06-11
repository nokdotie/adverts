package ie.nok.adverts.sherryFitzGerald

import ie.nok.adverts.Advert
import ie.nok.adverts.utils.Eircode
import ie.nok.adverts.utils.zio.Client
import org.jsoup.nodes.Document
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.http.model.Headers
import zio.stream.ZStream
import zio.json.{JsonDecoder, DeriveJsonDecoder}
import java.time.Instant

object Properties {
  case class Response(features: List[ResponseFeature])
  given JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  case class ResponseFeature(properties: ResponseFeatureProperty)
  given JsonDecoder[ResponseFeature] = DeriveJsonDecoder.gen[ResponseFeature]

  case class ResponseFeatureProperty(link: String)
  given JsonDecoder[ResponseFeatureProperty] =
    DeriveJsonDecoder.gen[ResponseFeatureProperty]

  private def getApiResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Response] = {
    val refererHeader = Headers("Referer", "https://www.sherryfitz.ie/")

    Client
      .requestJson[Response](url, headers = refererHeader)
      .retry(recurs(3) && fixed(1.second))
  }

  private def getHtmlResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Document] =
    Client
      .requestHtml(url)
      .retry(recurs(3) && fixed(1.second))

  private def parseHtmlResponse(url: String, html: Document): Option[Advert] = {
    val advertUrl = url

    val advertPrice = html
      .select(".property-price")
      .text
      .filter(_.isDigit)
      .toIntOption

    val propertyEircode = html
      .select(".property-address h1")
      .text
      .pipe(Eircode.regex.findFirstIn)
      .map(_.filter(_.isLetterOrDigit))

    val propertyImageUrls = html
      .select(".property-image-element img")
      .asScala
      .map { _.attr("src") }
      .toList

    val contactName = html.select(".agent-card-details-name a").text
    val contactPhone = html.select(".agent-card-details-contact-phone a").text
    val contactEmail = html.select(".agent-card-details-contact-email a").text

    advertPrice
      .zip(propertyEircode)
      .map { (advertPrice, propertyEircode) =>
        Advert(
          at = Instant.now(),
          advertUrl = advertUrl,
          advertPrice = advertPrice,
          propertyEircode = propertyEircode,
          propertyImageUrls = propertyImageUrls,
          contactName = contactName,
          contactPhone = Option(contactPhone),
          contactEmail = Option(contactEmail)
        )
      }
  }

  val stream: ZStream[ZioClient, Throwable, Advert] =
    ZStream
      .iterate(1)(_ + 1)
      .map { page =>
        s"https://www.sherryfitz.ie/sfdev/api/properties/?type=all&sort=created_at&order=desc&page=$page"
      }
      .mapZIOParUnordered(5) { getApiResponse }
      .map { _.features }
      .takeWhile { _.nonEmpty }
      .flattenIterables
      .map { feature =>
        s"https://www.sherryfitz.ie/${feature.properties.link}"
      }
      .mapZIOParUnordered(5) { url => getHtmlResponse(url).map { (url, _) } }
      .map { parseHtmlResponse.tupled }
      .collectSome

}
