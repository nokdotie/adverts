package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.adverts.Advert
import ie.nok.http.Client
import java.time.Instant
import org.jsoup.nodes.Document
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.chaining.scalaUtilChainingOps
import zio.{durationInt, ZIO}
import zio.Schedule.{recurs, fixed}
import zio.http.{Client => ZioClient}
import zio.stream.ZPipeline
import ie.nok.unit.{Area, AreaUnit}

object Property {

  private def getRequestUrl(link: String): String =
    s"https://www.sherryfitz.ie/$link"

  private def getResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Document] =
    Client
      .requestBodyAsHtml(url)
      .retry(recurs(3) && fixed(1.second))

  private def parseResponse(url: String, html: Document): Advert = {
    val price = html
      .select(".property-price")
      .text
      .filter(_.isDigit)
      .toIntOption
      .getOrElse(0)

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
      .fold(Area.empty) { Area(_, AreaUnit.SquareMetres) }

    val bedroomsCount = html
      .select(".property-stat:contains(bed)")
      .text
      .filter(_.isDigit)
      .toIntOption
      .getOrElse(0)

    val bathroomsCount = html
      .select(".property-stat:contains(bath)")
      .text
      .filter(_.isDigit)
      .toIntOption
      .getOrElse(0)

    Advert(
      advertUrl = url,
      advertPriceInEur = price,
      propertyAddress = address,
      propertyImageUrls = imageUrls,
      propertySize = size,
      propertySizeInSqtMtr = Area.toSquareMetres(size).value,
      propertyBedroomsCount = bedroomsCount,
      propertyBathroomsCount = bathroomsCount,
      createdAt = Instant.now()
    )
  }

  val pipeline: ZPipeline[ZioClient, Throwable, String, Advert] =
    ZPipeline
      .map { getRequestUrl }
      .mapZIOParUnordered(5) { url => getResponse(url).map { (url, _) } }
      .map { parseResponse.tupled }

}
