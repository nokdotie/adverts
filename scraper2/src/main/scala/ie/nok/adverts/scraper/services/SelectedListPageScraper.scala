package ie.nok.adverts.scraper.services

import java.net.URL
import org.jsoup.nodes.Document
import scala.util.chaining.scalaUtilChainingOps

trait SelectedListPageScraper extends ServiceListPageScraper {

  def getUrls(): List[URL]

  private def getUrlsZipWithNext() = getUrls()
    .sliding(2)
    .collect { case current :: next :: Nil => (current, next) }
    .toList

  override def getFirstPageUrl(): URL = getUrls().head
  override def getNextPageUrl(document: Document): Option[URL] = {
    val currentUrl = document.location().pipe { URL(_) }
    getUrlsZipWithNext()
      .find { case (url, _) => url == currentUrl }
      .map { case (_, next) => next }
  }

}
