package ie.nok.adverts.scraper.services

import java.net.URL
import org.jsoup.nodes.Document

trait ServiceListPageScraper {

  def getNextPageUrl(document: Document): Option[URL]
  def getItemPageUrls(document: Document): Iterable[URL]

}
