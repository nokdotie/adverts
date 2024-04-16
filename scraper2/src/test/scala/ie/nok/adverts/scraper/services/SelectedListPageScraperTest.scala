package ie.nok.adverts.scraper.services

import java.net.URL
import org.jsoup.nodes.Document

class SelectedListPageScraperTest extends munit.FunSuite {

  test("selected") {
    val scraper = new SelectedListPageScraper {

      override def getItemPageUrls(document: Document) = Iterable.empty

      override def getUrls() = List(
        URL("https://example.com/foo"),
        URL("https://example.com/bar"),
        URL("https://example.com/baz")
      )
    }

    val document    = Document("https://example.com/bar")
    val nextPageUrl = scraper.getNextPageUrl(document)

    assertEquals(nextPageUrl, Some(URL("https://example.com/baz")))
  }

}
