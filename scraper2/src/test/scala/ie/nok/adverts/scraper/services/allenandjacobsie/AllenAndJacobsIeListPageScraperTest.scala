package ie.nok.adverts.scraper.services.allenandjacobsie

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class AllenAndJacobsIeListPageScraperTest extends munit.FunSuite {

  test("sitemap") {
    val document = ScraperHelper.getDocument(
      "services/allenandjacobsie/lists/sitemap.xml",
      AllenAndJacobsIeListPageScraper.getFirstPageUrl().toString()
    )

    val results = AllenAndJacobsIeListPageScraper.getItemPageUrls(document)

    assertEquals(results.size, 420)
  }

}
