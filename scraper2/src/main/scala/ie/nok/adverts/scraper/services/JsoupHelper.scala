package ie.nok.adverts.scraper.services

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.safety.Safelist
import scala.jdk.CollectionConverters.*
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import scala.util.matching.Regex

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
    find(document, cssQuery)
      .map { _.text }
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
}
