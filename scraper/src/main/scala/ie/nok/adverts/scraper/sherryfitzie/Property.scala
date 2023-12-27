package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.adverts.Advert
import ie.nok.adverts.services.sherryfitzie.SherryFitzIeAdvert
import ie.nok.ber.Rating
import ie.nok.ecad.Eircode
import ie.nok.http.Client
import ie.nok.geographic.Coordinates
import ie.nok.unit.{Area, AreaUnit}
import java.time.Instant
import org.jsoup.nodes.Document
import scala.jdk.CollectionConverters._
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

  private def size(html: Document): Option[Area] =
    html
      .select(".property-stat:contains(sqm)")
      .text
      .filter(_.isDigit)
      .toIntOption
      .map { BigDecimal.apply }
      .map { Area(_, AreaUnit.SquareMetres) }

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

  private def toSherryFitzIeAdvert(
      url: String,
      coordinates: Coordinates,
      html: Document
  ): SherryFitzIeAdvert = {
    val description = html
      .select(".property-description")
      .tap { _.select("br").after("\\n"); }
      .text
      .replaceAll("\\\\n", "\n")
      .replace("*** Please register on www.mysherryfitz.ie to bid on this property***", "")

    val imageUrls = html
      .select(".property-image-element img")
      .asScala
      .map { _.attr("src") }
      .toList

    val (rating, certificateNumber, energyRating) = ber(html)

    val (address, eircode) = addressAndEircode(html)

    SherryFitzIeAdvert(
      url = url,
      priceInEur = documentToIntOption(html, ".property-price"),
      description = description,
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
