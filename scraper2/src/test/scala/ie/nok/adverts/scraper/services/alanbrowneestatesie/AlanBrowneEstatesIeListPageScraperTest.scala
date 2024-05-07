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
        URL("https://alanbrowneestates.ie/property.php?property=T12%20V4K3"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20C4HF"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20DH79"),
        URL("https://alanbrowneestates.ie/property.php?property=T12RRF6"),
        URL("https://alanbrowneestates.ie/property.php?property=T23%20A2YP"),
        URL("https://alanbrowneestates.ie/property.php?property=SiteCMAC"),
        URL("https://alanbrowneestates.ie/property.php?property=T45%20CK11"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20K0H6"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20RP7Y"),
        URL("https://alanbrowneestates.ie/property.php?property=T12KP04"),
        URL("https://alanbrowneestates.ie/property.php?property=T23%20NXK0"),
        URL("https://alanbrowneestates.ie/property.php?property=T45%20YE84"),
        URL("https://alanbrowneestates.ie/property.php?property=T45%20PF90"),
        URL("https://alanbrowneestates.ie/property.php?property=E34%20Y236"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20YY2H"),
        URL("https://alanbrowneestates.ie/property.php?property=P36%20YC64"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20YD5C"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20CY9Y"),
        URL("https://alanbrowneestates.ie/property.php?property=T12%20HHN7"),
        URL("https://alanbrowneestates.ie/property.php?property=P24%20V098"),
        URL("https://alanbrowneestates.ie/property.php?property=P24%20K521"),
        URL("https://alanbrowneestates.ie/property.php?property=P51%20H0VY"),
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
