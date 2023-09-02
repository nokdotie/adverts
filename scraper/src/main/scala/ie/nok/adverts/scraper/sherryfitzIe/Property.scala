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

  private val parseResponseBuildingEnergyRating
      : (Document, AdvertSource) => Iterable[AdvertAttribute] = {
    val cssQuery =
      ".property-full-description-container:contains(BER) .property-description"
    val berRegex = "BER: ([A-G][1-3]?)".r
    val berCertificateNumberRegex = "BER Number: ([0-9]+)".r
    val berEnergyRatingRegex =
      "Energy Performance Indicator: ([0-9]+\\.?[0-9]+)".r

    (html, source) => {
      val text = html
        .select(cssQuery)
        .text

      berRegex
        .findFirstMatchIn(text)
        .map { _.group(1) }
        .flatMap { Rating.tryFromString(_).toOption }
        .map { _.toString }
        .map { AdvertAttribute.BuildingEnergyRating(_, source) }
        ++ berCertificateNumberRegex
          .findFirstMatchIn(text)
          .flatMap { _.group(1).toIntOption }
          .map {
            AdvertAttribute.BuildingEnergyRatingCertificateNumber(_, source)
          }
        ++ berEnergyRatingRegex
          .findFirstMatchIn(text)
          .map { _.group(1) }
          .flatMap { value => Try { BigDecimal(value) }.toOption }
          .map {
            AdvertAttribute
              .BuildingEnergyRatingEnergyRatingInKWhPerSqtMtrPerYear(_, source)
          }
    }
  }

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

    val source = AdvertSource(
      service = AdvertService.SherryFitzIe,
      url = url
    )

    val attributes = List(
      AdvertAttribute.Address(address, source),
      AdvertAttribute.Coordinates(coordinates, source)
    ) ++ price.map { AdvertAttribute.PriceInEur(_, source) }
      ++ imageUrls.map { AdvertAttribute.ImageUrl(_, source) }
      ++ size.map(_.value).map { AdvertAttribute.SizeInSqtMtr(_, source) }
      ++ bedroomsCount.map { AdvertAttribute.BedroomsCount(_, source) }
      ++ bathroomsCount.map { AdvertAttribute.BathroomsCount(_, source) }
      ++ parseResponseBuildingEnergyRating(html, source)

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
      attributes = attributes,
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
