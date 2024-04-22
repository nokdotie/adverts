package ie.nok.adverts.scraper.services.maherpropertyie

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class MaherPropertyIeListPageScraperTest extends munit.FunSuite {

  test("Yoast sitemap") {
    ScraperHelper.assertListPageScraperResults(
      "services/maherpropertyie/lists/property-sitemap.xml",
      "https://maherproperty.ie/property-sitemap.xml",
      MaherPropertyIeListPageScraper,
      None,
      List(
        URL("https://maherproperty.ie/property/high-street-bagenalstown-co-carlow/"),
        URL("https://maherproperty.ie/property/clogrennane-carlow-town-carlow/"),
        URL("https://maherproperty.ie/property/5-barrowville-kilkenny-road-carlow-town-carlow-r93d8p9/"),
        URL("https://maherproperty.ie/property/56-the-meadows-bullock-park-carlow/"),
        URL("https://maherproperty.ie/property/18-millbrook-mill-lane-carlow-town-co-carlow/"),
        URL("https://maherproperty.ie/property/4-chaff-street-graiguecullen-carlow-r93-y327/"),
        URL("https://maherproperty.ie/property/7-clarence-gate-kilkenny-road-carlow-town-co-carlow/"),
        URL("https://maherproperty.ie/property/23-college-green-carlow-town-co-carlow/"),
        URL("https://maherproperty.ie/property/41-crosbie-place-barrack-street-carlow-town-co-carlow/"),
        URL("https://maherproperty.ie/property/94-highfield-manor-graiguecullen-carlow-co-carlow/"),
        URL("https://maherproperty.ie/property/apartment-24-ferrybank-apartment-complex-leighlin-road-carlow-town-co-carlow-r93a499/"),
        URL("https://maherproperty.ie/property/26-the-weirs-leighlin-road-graiguecullen-co-carlow/"),
        URL("https://maherproperty.ie/property/38-oakley-park-tullow-road-carlow-town-co-carlow-r93x9f6/"),
        URL("https://maherproperty.ie/property/21-beech-road-rivercourt-carlow-town-co-carlow-r93fp29/"),
        URL("https://maherproperty.ie/property/28-spindlewood-graiguecullen-carlow-r93-w280/"),
        URL("https://maherproperty.ie/property/73-feltham-hall-blackbog-road-carlow/"),
        URL("https://maherproperty.ie/property/67-ashfield-blackbog-road-carlow-r93-x8c4/"),
        URL("https://maherproperty.ie/property/16-castleview-castle-view-quay-garaiguecullen-carlow-r93d2v8/"),
        URL("https://maherproperty.ie/property/21-killerig-golf-lodges-killerig-co-carlow/"),
        URL("https://maherproperty.ie/property/apts-1-7-nelson-street-athy-athy-kildare-r14p403/")
      )
    )
  }
}
