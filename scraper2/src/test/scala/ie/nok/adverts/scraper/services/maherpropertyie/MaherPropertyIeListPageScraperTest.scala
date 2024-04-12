package ie.nok.adverts.scraper.services.maherpropertyie

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class MaherPropertyIeListPageScraperTest extends munit.FunSuite {

  test("first page") {
    ScraperHelper.assertListPageScraperResults(
      "services/maherpropertyie/lists/first.html",
      "https://maherproperty.ie/property-search/?status=residential-sales",
      MaherPropertyIeListPageScraper,
      Some(URL("https://maherproperty.ie/property-search/page/2/?status=residential-sales")),
      List(
        URL("https://maherproperty.ie/property/67-ashfield-blackbog-road-carlow-r93-x8c4/"),
        URL("https://maherproperty.ie/property/16-castleview-castle-view-quay-garaiguecullen-carlow-r93d2v8/"),
        URL("https://maherproperty.ie/property/28-spindlewood-graiguecullen-carlow-r93-w280/"),
        URL("https://maherproperty.ie/property/73-feltham-hall-blackbog-road-carlow/")
      )
    )
  }

  test("last page") {
    ScraperHelper.assertListPageScraperResults(
      "services/maherpropertyie/lists/last.html",
      "https://maherproperty.ie/property-search/page/4/?status=residential-sales",
      MaherPropertyIeListPageScraper,
      None,
      List(
        URL("https://maherproperty.ie/property/18-millbrook-mill-lane-carlow-town-co-carlow/"),
        URL("https://maherproperty.ie/property/4-chaff-street-graiguecullen-carlow-r93-y327/"),
        URL("https://maherproperty.ie/property/5-barrowville-kilkenny-road-carlow-town-carlow-r93d8p9/"),
        URL("https://maherproperty.ie/property/7-clarence-gate-kilkenny-road-carlow-town-co-carlow/")
      )
    )
  }
}
