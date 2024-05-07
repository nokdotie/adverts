package ie.nok.adverts.scraper.services.alanbrowneestatesie

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class AlanBrowneEstatesIeListPageScraperTest extends munit.FunSuite {

  test("first page") {
    ScraperHelper.assertListPageScraperResults(
      "services/alanbrowneestatesie/lists/first.html",
      AlanBrowneEstatesIeListPageScraper.getFirstPageUrl().toString(),
      AlanBrowneEstatesIeListPageScraper,
      None,
      List(
        URL("https://alanbrowneestates.ie/property.php?property=T12 V4K3"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 C4HF"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 DH79"),
        URL("https://alanbrowneestates.ie/property.php?property=T12RRF6"),
        URL("https://alanbrowneestates.ie/property.php?property=T23 A2YP"),
        URL("https://alanbrowneestates.ie/property.php?property=SiteCMAC"),
        URL("https://alanbrowneestates.ie/property.php?property=T45 CK11"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 K0H6"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 RP7Y"),
        URL("https://alanbrowneestates.ie/property.php?property=T12KP04"),
        URL("https://alanbrowneestates.ie/property.php?property=T23 NXK0"),
        URL("https://alanbrowneestates.ie/property.php?property=T45 YE84"),
        URL("https://alanbrowneestates.ie/property.php?property=T45 PF90"),
        URL("https://alanbrowneestates.ie/property.php?property=E34 Y236"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 YY2H"),
        URL("https://alanbrowneestates.ie/property.php?property=P36 YC64"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 YD5C"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 CY9Y"),
        URL("https://alanbrowneestates.ie/property.php?property=T12 HHN7"),
        URL("https://alanbrowneestates.ie/property.php?property=P24 V098"),
        URL("https://alanbrowneestates.ie/property.php?property=P24 K521"),
        URL("https://alanbrowneestates.ie/property.php?property=P51 H0VY"),
        URL("https://alanbrowneestates.ie/property.php?property=P24 PH04"),
        URL("https://alanbrowneestates.ie/property.php?property=P24 FK75"),
        URL("https://alanbrowneestates.ie/property.php?property=P24 RT29"),
        URL("https://alanbrowneestates.ie/property.php?property=P24 Y049"),
        URL("https://alanbrowneestates.ie/property.php?property=P24 Y296"),
        URL("https://alanbrowneestates.ie/property.php?property=P51 XKR8")
      )
    )
  }

}
