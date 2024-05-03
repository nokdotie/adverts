package ie.nok.adverts.scraper.services

import java.io.File
import java.nio.file.Files
import java.net.URL
import ie.nok.adverts.Advert
import munit.Assertions.assertEquals
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.io.Source
import scala.util.Using
import scala.util.chaining.scalaUtilChainingOps

object ScraperHelper {

  def getDocument(resourcePath: String, location: String): Document = {
    def download(): Unit = {
      val resourcePathFromRoot = s"scraper2/src/test/resources/$resourcePath"
      val file                 = File(resourcePathFromRoot)

      if (!file.exists()) {
        val response = Jsoup.connect(location).execute()
        val bytes    = response.bodyAsBytes()

        file.getParentFile().mkdirs()
        file.createNewFile()
        Files.write(file.toPath(), bytes)
      }
    }

    def parse(): Document = {
      val resource = Source.fromResource(resourcePath)
      val html     = Using.resource(resource) { _.mkString }
      val document = Jsoup.parse(html, location)

      document
    }

    download()
    parse()
  }

  def assertListPageScraperResults(
      resourcePath: String,
      location: String,
      listPageScraper: ServiceListPageScraper,
      expectedNextPageUrl: Option[URL],
      expectedItemPageUrls: List[URL]
  ): Unit = {
    val document = getDocument(resourcePath, location)

    val obtainedNextPageUrl = listPageScraper.getNextPageUrl(document)
    assertEquals(obtainedNextPageUrl, expectedNextPageUrl)

    val obtainedItemPageUrls = listPageScraper.getItemPageUrls(document)
    assertEquals(obtainedItemPageUrls, expectedItemPageUrls)
  }

  def assertItemPageScraperResults(
      resourcePath: String,
      location: String,
      itemPageScraper: ServiceItemPageScraper,
      expectedAdvert: Advert
  ): Unit = {
    val document = getDocument(resourcePath, location)

    val obtainedAdvert = itemPageScraper
      .getAdvert(document)
      .copy(createdAt = expectedAdvert.createdAt)

    assertEquals(obtainedAdvert, expectedAdvert)
  }

}
