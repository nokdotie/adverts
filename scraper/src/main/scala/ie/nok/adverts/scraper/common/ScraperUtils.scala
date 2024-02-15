package ie.nok.adverts.scraper.common
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.util.chaining.scalaUtilChainingOps

object ScraperUtils {

  def htmlToPlainText(inputHTML: String): String =
    htmlToPlainText(Jsoup.parse(inputHTML).select("body"))

  def htmlToPlainText(elements: Elements): String = {
    elements
      .tap { _.select("br").before("\\n") }
      .tap { _.select("p").before("\\n") }
      .tap { _.select("li").before("\\n- ") }
      .text()
      .replaceAll("\\\\n", "\n")
      .linesIterator
      .map(_.trim)
      .mkString("\n")
      .trim
  }
}
