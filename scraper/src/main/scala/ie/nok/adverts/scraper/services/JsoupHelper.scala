package ie.nok.adverts.scraper.services

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.safety.Safelist
import scala.jdk.CollectionConverters.*
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps

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

  def findInt(document: Document, cssQuery: String): Option[Int] =
    findString(document, cssQuery).flatMap { _.filter { _.isDigit }.toIntOption }

  private def findAttribute(document: Document, cssQuery: String, attributeKey: String): Option[String] =
    find(document, cssQuery)
      .map { _.attr(attributeKey) }

  def findAttributeHref(document: Document, cssQuery: String): Option[String] =
    findAttribute(document, cssQuery, "href")

  def findAttributeAlt(document: Document, cssQuery: String): Option[String] =
    findAttribute(document, cssQuery, "alt")

  def filterAttributesSrc(document: Document, cssQuery: String): List[String] =
    document
      .select(cssQuery)
      .asScala
      .map { _.attr("src") }
      .toList
}
