package ie.deed.websites

import ie.deed.{Scraper, Record, Eircode}
import collection.convert.ImplicitConversions.*
import util.chaining.scalaUtilChainingOps
import zio.{ZIO, Console}
import zio.stream.ZStream
import zio.json._
import zio.http.model.Headers
import zio.http.{Client, ZClient, Body, Response}
import org.jsoup.Jsoup
import java.time.Instant

object SherryFitzIe extends Scraper:
  case class PropertiesApiResponse(
      features: List[PropertiesApiResponseFeature]
  )
  object PropertiesApiResponse {
    implicit val decoder: JsonDecoder[PropertiesApiResponse] =
      DeriveJsonDecoder.gen[PropertiesApiResponse]
  }
  case class PropertiesApiResponseFeature(
      properties: PropertiesApiResponseFeatureProperty
  )
  object PropertiesApiResponseFeature {
    implicit val decoder: JsonDecoder[PropertiesApiResponseFeature] =
      DeriveJsonDecoder.gen[PropertiesApiResponseFeature]
  }
  case class PropertiesApiResponseFeatureProperty(link: String)
  object PropertiesApiResponseFeatureProperty {
    implicit val decoder: JsonDecoder[PropertiesApiResponseFeatureProperty] =
      DeriveJsonDecoder.gen[PropertiesApiResponseFeatureProperty]
  }

  private val getPropertiesApiLinks: ZStream[Any, Nothing, String] =
    ZStream
      .iterate(1)(_ + 1)
      .map { page =>
        s"https://www.sherryfitz.ie/sfdev/api/properties/?type=all&sort=created_at&order=desc&page=$page"
      }

  private def getPropertiesFromApi(
      url: String
  ): ZIO[Client, Throwable, PropertiesApiResponse] =
    val refererHeader = Headers("Referer", "https://www.sherryfitz.ie/")

    Client
      .request(url, headers = refererHeader)
      .retryN(3)
      .flatMap { _.body.asString }
      .flatMap {
        _.fromJson[PropertiesApiResponse].left
          .map(Throwable(_))
          .pipe(ZIO.fromEither)
      }

  private def getPropertyHtmlLink(
      feature: PropertiesApiResponseFeature
  ): String =
    s"https://www.sherryfitz.ie/${feature.properties.link}"

  private def getPropertyFromHtml(
      url: String
  ): ZIO[Client, Throwable, Option[Record]] =
    Client
      .request(url)
      .retryN(3)
      .flatMap { _.body.asString }
      .flatMap { html => ZIO.attempt { Jsoup.parse(html) } }
      .map { doc =>
        val advertUrl = url

        val advertPrice = doc
          .select(".property-price")
          .text
          .filter(_.isDigit)
          .toIntOption

        val propertyEircode = doc
          .select(".property-address h1")
          .text
          .pipe(Eircode.regex.findFirstIn)
          .map(_.filter(_.isLetterOrDigit))

        val propertyImageUrls = doc
          .select(".property-image-element img")
          .map { _.attr("src") }
          .toList

        val contactName = doc.select(".agent-card-details-name a").text
        val contactPhone =
          doc.select(".agent-card-details-contact-phone a").text
        val contactEmail =
          doc.select(".agent-card-details-contact-email a").text

        advertPrice
          .zip(propertyEircode)
          .map { (advertPrice, propertyEircode) =>
            Record(
              at = Instant.now(),
              advertUrl = advertUrl,
              advertPrice = advertPrice,
              propertyEircode = propertyEircode,
              propertyImageUrls = propertyImageUrls,
              contactName = contactName,
              contactPhone = contactPhone,
              contactEmail = contactEmail
            )
          }
      }

  val scrape = getPropertiesApiLinks
    .mapZIOPar(5) { getPropertiesFromApi }
    .map { _.features }
    .takeWhile { _.nonEmpty }
    .flattenIterables
    .map { getPropertyHtmlLink }
    .mapZIOPar(5) { url =>
      getPropertyFromHtml(url)
        .tapSome { case None =>
          Console.printLineError(s"Failed to parse SherryFitzIe: $url")
        }
    }
    .collectSome
