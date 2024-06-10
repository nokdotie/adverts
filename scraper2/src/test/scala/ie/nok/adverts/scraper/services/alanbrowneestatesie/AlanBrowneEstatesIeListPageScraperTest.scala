package ie.nok.adverts.scraper.services.alanbrowneestatesie

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class AlanBrowneEstatesIeListPageScraperTest extends munit.FunSuite {

  test("first page") {
    ScraperHelper.assertListPageScraperResults(
      "services/alanbrowneestatesie/lists/first.html",
      AlanBrowneEstatesIeListPageScraper.getFirstPageUrl().toString(),
      AlanBrowneEstatesIeListPageScraper,
      Some(URL("https://alanbrowneestates.ie/properties.php?page=2")),
      List(
        URL("https://alanbrowneestates.ie/property.php?property=T12A9K5"),
        URL("https://alanbrowneestates.ie/property.php?property=T45%20CX38"),
        URL("https://alanbrowneestates.ie/property.php?property=T23%20X9T7"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20V4K3"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20C4HF"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20DH79"),
        URL("https://alanbrowneestates.ie/property.php?property=T23%20A2YP"),
        URL("https://alanbrowneestates.ie/property.php?property=SiteCMAC"),
        URL("https://alanbrowneestates.ie/property.php?property=T45%20CK11"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20K0H6"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20RP7Y"),
        URL("https://alanbrowneestates.ie/property.php?property=T12KP04")
      )
    )
  }

  test("last page") {
    ScraperHelper.assertListPageScraperResults(
      "services/alanbrowneestatesie/lists/last.html",
      "https://alanbrowneestates.ie/properties.php?page=3",
      AlanBrowneEstatesIeListPageScraper,
      None,
      List(
        URL("https://alanbrowneestates.ie/property.php?property=P24%20PH04"),
        URL("https://alanbrowneestates.ie/property.php?property=P24%20FK75"),
        URL("https://alanbrowneestates.ie/property.php?property=P24%20RT29"),
        URL("https://alanbrowneestates.ie/property.php?property=P24%20Y049"),
        URL("https://alanbrowneestates.ie/property.php?property=P24%20Y296"),
        URL("https://alanbrowneestates.ie/property.php?property=P51%20XKR8")
      )
    )
  }

}
