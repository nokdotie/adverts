package ie.nok.adverts.scraper.services

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist
import scala.jdk.CollectionConverters.*
import scala.util.chaining.scalaUtilChainingOps

object JsoupHelper {
  def selectFirstString(document: Document, cssQuery: String): String =
    document
      .selectFirst(cssQuery)
      .text

  def selectFirstStringKeepNewLine(document: Document, cssQuery: String): String =
    document
      .outputSettings(jsoupOutputSettingsKeepNewLine)
      .selectFirst(cssQuery)
      .html()
      .pipe { Jsoup.clean(_, "", Safelist.none(), jsoupOutputSettingsKeepNewLine) }

  private val jsoupOutputSettingsKeepNewLine = Document
    .OutputSettings()
    .prettyPrint(false)
  def selectFirstStringWithLineBreaks(document: Document, cssQuery: String): String = {
    val prettyPrintOff = Document.OutputSettings().prettyPrint(false)
    val html = document
      .outputSettings(prettyPrintOff)
      .tap { _.select("br").before("\\n") }
      .tap { _.select("p").before("\\n") }
      .tap { _.select("li").before("\\n- ") }
      .html
      .replaceAll("\\\\n", "\n")

    Jsoup.clean(html, "", Safelist.none(), prettyPrintOff)
  }

  def selectFirstInt(document: Document, cssQuery: String): Option[Int] =
    selectFirstString(document, cssQuery).filter { _.isDigit }.toIntOption

  def selectFirstAttribute(document: Document, cssQuery: String, attributeKey: String): String =
    document
      .selectFirst(cssQuery)
      .attr(attributeKey)

  def selectFirstHrefAttribute(document: Document, cssQuery: String): String =
    selectFirstAttribute(document, cssQuery, "href")

  def selectFirstAltAttribute(document: Document, cssQuery: String): String =
    selectFirstAttribute(document, cssQuery, "alt")

  def selectFirstSrcAttribute(document: Document, cssQuery: String): String =
    selectFirstAttribute(document, cssQuery, "src")

  def selectAllSrcAttributes(document: Document, cssQuery: String): List[String] =
    document
      .select(cssQuery)
      .asScala
      .map { _.attr("src") }
      .toList
}
