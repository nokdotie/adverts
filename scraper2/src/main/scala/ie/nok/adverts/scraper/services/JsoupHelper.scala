package ie.nok.adverts.scraper.services

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.safety.Safelist
import scala.jdk.CollectionConverters.*
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import scala.util.matching.Regex
import ie.nok.geographic.Coordinates

object JsoupHelper {
  def find(document: Document, cssQuery: String): Option[Element] =
    Option(document.selectFirst(cssQuery))

  def findString(document: Document, cssQuery: String): Option[String] =
    find(document, cssQuery)
      .map { _.text }

  private val jsoupOutputSettingsKeepNewLine = Document
    .OutputSettings()
    .prettyPrint(false)
  def findStringKeepLineBreaks(document: Document, cssQuery: String): Option[String] = {
    val prettyPrintOff = Document.OutputSettings().prettyPrint(false)

    find(document.outputSettings(prettyPrintOff), cssQuery)
      .map {
        _.tap { _.select("br").before("\\n") }
          .tap { _.select("p").before("\\n") }
          .tap { _.select("li").before("\\n- ") }
          .html
          .replaceAll("\\\\n", "\n")
      }
      .map { Jsoup.clean(_, "", Safelist.none(), prettyPrintOff) }
  }

  def findRegex(document: Document, cssQuery: String, regex: Regex): Option[Regex.Match] =
    findString(document, cssQuery)
      .flatMap { regex.findFirstMatchIn }

  private def findAttribute(document: Document, cssQuery: String, attributeKey: String): Option[String] =
    find(document, cssQuery)
      .map { _.attr(attributeKey) }

  def findAttributeAlt(document: Document, cssQuery: String): Option[String] =
    findAttribute(document, cssQuery, "alt")

  private def findAttributeUrl(document: Document, cssQuery: String, attributeKey: String): Option[String] =
    find(document, cssQuery)
      .map { _.absUrl(attributeKey) }

  def findAttributeHref(document: Document, cssQuery: String): Option[String] =
    findAttributeUrl(document, cssQuery, "href")

  def filterStrings(document: Document, cssQuery: String): List[String] =
    document
      .select(cssQuery)
      .asScala
      .map { _.text }
      .toList

  def filterRegex(document: Document, cssQuery: String, regex: Regex): List[Regex.Match] =
    filterStrings(document, cssQuery)
      .flatMap { regex.findAllMatchIn }

  private def filterAttributesUrls(document: Document, cssQuery: String, attributeKey: String): List[String] =
    document
      .select(cssQuery)
      .asScala
      .map { _.absUrl(attributeKey) }
      .toList

  def filterAttributesHref(document: Document, cssQuery: String): List[String] =
    filterAttributesUrls(document, cssQuery, "href")

  def filterAttributesSrc(document: Document, cssQuery: String): List[String] =
    filterAttributesUrls(document, cssQuery, "src")

  private val googleMapsCssQuery = "a[href^=https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=]"
  private val googleMapsRegex    = raw"https:\/\/www\.google\.com\/maps\/@\?api=1&map_action=pano&viewpoint=(-?\d+\.?\d+),(-?\d+\.?\d+)".r
  def findGoogleMapsCoordinates(document: Document): Option[Coordinates] =
    JsoupHelper
      .findAttributeHref(document, googleMapsCssQuery)
      .flatMap { googleMapsRegex.findFirstMatchIn }
      .map { coordinates =>
        val latitude  = coordinates.group(1).pipe { BigDecimal(_) }
        val longitude = coordinates.group(2).pipe { BigDecimal(_) }

        Coordinates(latitude = latitude, longitude = longitude)
      }

}
