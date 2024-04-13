package ie.nok.adverts.scraper.services

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class SitemapListPageScraperTest extends munit.FunSuite {

  test("sitemap") {
    ScraperHelper.assertListPageScraperResults(
      "services/lists/sitemap.xml",
      "https://www.myhome.ie/sitemaps/residentialforsale_sitemap.xml",
      SitemapListPageScraper,
      None,
      List(
        URL("https://www.myhome.ie/residential/brochure/59-new-road-clondalkin-dublin-22/4785119"),
        URL("https://www.myhome.ie/residential/brochure/apt-8-cois-abhainn-clane-co-kildare/4784321"),
        URL("https://www.myhome.ie/residential/brochure/52-shrewsbury-ballsbridge-dublin-4/4785390")
      )
    )
  }

}
