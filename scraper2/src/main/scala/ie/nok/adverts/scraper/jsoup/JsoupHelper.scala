package ie.nok.adverts.scraper.jsoup

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
    val newLinePlaceholder = "NEW_LINE_PLACEHOLDER"
    val prettyPrintOff     = Document.OutputSettings().prettyPrint(false)

    find(document.outputSettings(prettyPrintOff), cssQuery)
      .map {
        _.tap { _.select("br").after(newLinePlaceholder) }
          .tap { _.select("div").after(newLinePlaceholder) }
          .tap { _.select("p").after(newLinePlaceholder) }
          .tap { _.select("li").before("- ").after(newLinePlaceholder) }
          .html
          .replaceAll(newLinePlaceholder, "\n")
          .replaceAll("&nbsp;", " ")
          .replaceAll(" +", " ")
      }
      .map { Jsoup.clean(_, "", Safelist.none(), prettyPrintOff) }
      .map {
        _.linesIterator
          .map { _.trim }
          .mkString("\n")
          .replaceAll("\\n\\n\\n+", "\n\n\n")
      }
      .map { _.trim }
  }

  def findRegex(document: Document, cssQuery: String, regex: Regex): Option[Regex.Match] =
    filterRegex(document, cssQuery, regex).headOption

  private def findAttribute(document: Document, cssQuery: String, attributeKey: String): Option[String] =
    find(document, cssQuery)
      .map { _.attr(attributeKey) }

  def findAttributeAlt(document: Document, cssQuery: String): Option[String] =
    findAttribute(document, cssQuery, "alt")

  def findAttributeContent(document: Document, cssQuery: String): Option[String] =
    findAttribute(document, cssQuery, "content")

  def findAttributeValue(document: Document, cssQuery: String): Option[String] =
    findAttribute(document, cssQuery, "value")

  private def findAttributeUrl(document: Document, cssQuery: String, attributeKey: String): Option[String] =
    find(document, cssQuery)
      .map { _.absUrl(attributeKey) }

  def findAttributeHref(document: Document, cssQuery: String): Option[String] =
    findAttributeUrl(document, cssQuery, "href")

  def findAttributeClass(document: Document, cssQuery: String): Option[String] =
    findAttribute(document, cssQuery, "class")

  def filterStrings(document: Document, cssQuery: String): List[String] =
    document
      .select(cssQuery)
      .asScala
      .map { _.text }
      .toList

  def filterRegex(document: Document, cssQuery: String, regex: Regex): List[Regex.Match] =
    filterStrings(document, cssQuery)
      .flatMap { regex.findAllMatchIn }

  private def filterAttributes(document: Document, cssQuery: String, attributeKey: String): List[String] =
    document
      .select(cssQuery)
      .asScala
      .map { _.attr(attributeKey) }
      .toList

  def filterAttributesStyleBackgroundImage(document: Document, cssQuery: String): List[String] =
    filterAttributes(document, cssQuery, "style")
      .flatMap {
        case s"background-image: url($url)" => Some(url)
        case _                              => None
      }

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
