package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.adverts.scraper.common.ScraperUtils
import ie.nok.adverts.services.sherryfitzie.SherryFitzIeAdvert
import ie.nok.adverts.{Advert, PropertyType}
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.geographic.Coordinates
import ie.nok.http.Client
import ie.nok.unit.{Area, AreaUnit}
import org.jsoup.nodes.Document
import zio.Schedule.{fixed, recurs}
import zio.http.Client as ZioClient
import zio.stream.ZPipeline
import zio.{ZIO, durationInt}

import java.time.Instant
import scala.jdk.CollectionConverters.*
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps

object Property {

  private def getRequestUrl(link: String): String =
    s"https://www.sherryfitz.ie/$link"

  private def getResponse(
      url: String
  ): ZIO[ZioClient, Throwable, Document] =
    Client
      .requestBodyAsHtml(url)
      .retry(recurs(3) && fixed(1.second))

  private def documentToIntOption(
      html: Document,
      cssQuery: String
  ): Option[Int] =
    html
      .select(cssQuery)
      .text
      .filter(_.isDigit)
      .toIntOption

  private def addressAndEircode(html: Document): (String, Option[Eircode]) =
    html
      .select(".property-address h1")
      .first
      .pipe { Option.apply }
      .fold("") { _.wholeText }
      .linesIterator
      .map { _.trim }
      .filter { _.nonEmpty }
      .mkString(", ")
      .pipe(Eircode.unzip)

  private def size(html: Document): Option[Area] = {
    val areaText     = html.select(".property-stat:contains(sqm),.property-stat:contains(Acres)").text()
    val valueAndUnit = areaText.split(" ").filter(_.nonEmpty)
    val value        = valueAndUnit.headOption.map { BigDecimal(_) }
    val unit = valueAndUnit.lastOption.map {
      case "sqm"   => AreaUnit.SquareMetres
      case "Acres" => AreaUnit.Acres
      case other   => throw new Exception(s"Unknown unit: $other")
    }
    value.zip(unit).map { Area(_, _) }
  }

  private val ber: Document => (Option[Rating], Option[Int], Option[BigDecimal]) = {
    val cssQuery =
      ".property-full-description-container:contains(BER) .property-description"
    val berRegex                  = "BER: ([A-G][1-3]?)".r
    val berCertificateNumberRegex = "BER Number: ([0-9]+)".r
    val berEnergyRatingRegex =
      "Energy Performance Indicator: ([0-9]+\\.?[0-9]+)".r

    html => {
      val text = html
        .select(cssQuery)
        .text

      (
        berRegex
          .findFirstMatchIn(text)
          .map { _.group(1) }
          .flatMap { Rating.tryFromString(_).toOption },
        berCertificateNumberRegex
          .findFirstMatchIn(text)
          .flatMap { _.group(1).toIntOption },
        berEnergyRatingRegex
          .findFirstMatchIn(text)
          .map { _.group(1) }
          .flatMap { value => Try { BigDecimal(value) }.toOption }
      )
    }
  }

  protected[sherryfitzie] def toSherryFitzIeAdvert(
      url: String,
      coordinates: Coordinates,
      html: Document
  ): SherryFitzIeAdvert = {
    val description = html
      .select(".property-description")
      .pipe { v => ScraperUtils.htmlToPlainText(v) }
      .replace("*** Please register on www.mysherryfitz.ie to bid on this property***", "")

    val imageUrls = html
      .select(".property-image-element img")
      .asScala
      .map { _.attr("src") }
      .toList

    val (rating, certificateNumber, energyRating) = ber(html)

    val (address, eircode) = addressAndEircode(html)

    val propertyType: Option[PropertyType] = url
      .replace("https://www.sherryfitz.ie/buy/", "")
      .split("/")
      .headOption
      .flatMap(propertyType => PropertyType.tryFromString(propertyType.capitalize).toOption)

    SherryFitzIeAdvert(
      url = url,
      priceInEur = documentToIntOption(html, ".property-price"),
      description = description,
      propertyType = propertyType,
      address = address,
      eircode = eircode,
      coordinates = coordinates,
      imageUrls = imageUrls,
      size = size(html),
      bedroomsCount = documentToIntOption(html, ".property-stat:contains(bed)"),
      bathroomsCount = documentToIntOption(html, ".property-stat:contains(bath)"),
      buildingEnergyRating = rating,
      buildingEnergyRatingCertificateNumber = certificateNumber,
      buildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear = energyRating,
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
      .map { toSherryFitzIeAdvert.tupled }
      .map { SherryFitzIeAdvert.toAdvert }

}
