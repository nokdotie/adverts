package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.ber.Rating
import ie.nok.adverts._
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import org.jsoup.nodes.Document
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.stream.ZPipeline

object Property {

  private def getRequestUrl(link: String): String =
    s"https://www.sherryfitz.ie/$link"

  private def getResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Document] =
    Client
      .requestBodyAsHtml(url)
      .retry(recurs(3) && fixed(1.second))

  private def parseResponse(
      url: String,
      coordinates: Coordinates,
      html: Document
  ): Advert = {
    val price = html
      .select(".property-price")
      .text
      .filter(_.isDigit)
      .toIntOption

    val address = html
      .select(".property-address h1")
      .first
      .pipe { Option.apply }
      .fold("") { _.wholeText }
      .linesIterator
      .map { _.trim }
      .filter { _.nonEmpty }
      .mkString(", ")

    val imageUrls = html
      .select(".property-image-element img")
      .asScala
      .map { _.attr("src") }
      .toList

    val size = html
      .select(".property-stat:contains(sqm)")
      .text
      .filter(_.isDigit)
      .toIntOption
      .map { BigDecimal.apply }
      .map { Area(_, AreaUnit.SquareMetres) }

    val bedroomsCount = html
      .select(".property-stat:contains(bed)")
      .text
      .filter(_.isDigit)
      .toIntOption

    val bathroomsCount = html
      .select(".property-stat:contains(bath)")
      .text
      .filter(_.isDigit)
      .toIntOption

    Advert(
      advertUrl = url,
      advertPriceInEur = price.getOrElse(0),
      propertyAddress = address,
      propertyCoordinates = coordinates,
      propertyImageUrls = imageUrls,
      propertySize = size.getOrElse(Area.zero),
      propertySizeInSqtMtr = size.map(_.value).getOrElse(0),
      propertyBedroomsCount = bedroomsCount.getOrElse(0),
      propertyBathroomsCount = bathroomsCount.getOrElse(0),
      createdAt = Instant.now()
    )
  }

  val pipeline: ZPipeline[ZioClient, Throwable, (String, Coordinates), Advert] =
    ZPipeline
      .map[(String, Coordinates), (String, Coordinates)] { (link, coordinate) =>
        (getRequestUrl(link), coordinate)
      }
      .mapZIOParUnordered(5) { (url, coordinate) =>
        getResponse(url).map { (url, coordinate, _) }
      }
      .map { parseResponse.tupled }

}
