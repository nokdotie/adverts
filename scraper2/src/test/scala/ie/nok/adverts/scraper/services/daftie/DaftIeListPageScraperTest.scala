package ie.nok.adverts.scraper.services.daftie

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class DaftIeListPageScraperTest extends munit.FunSuite {

  test("first page") {
    ScraperHelper.assertListPageScraperResults(
      "services/daftie/lists/first.html",
      "https://www.daft.ie/property-for-sale/ireland",
      DaftIeListPageScraper,
      Some(URL("https://www.daft.ie/property-for-sale/ireland?from=20")),
      List(
        URL("https://www.daft.ie/for-sale/terraced-house-91-great-william-obrien-street-blackpool-blackpool-co-cork/5611174"),
        URL("https://www.daft.ie/for-sale/terraced-house-8-the-ferns-foxwood-rochestown-co-cork/5608255"),
        URL("https://www.daft.ie/for-sale/detached-house-moneymore-dunlewy-co-donegal/5590770"),
        URL("https://www.daft.ie/for-sale/apartment-apartment-8-reeves-hall-cork-city-co-cork/5576305"),
        URL("https://www.daft.ie/for-sale/detached-house-derryreel-dunfanaghy-co-donegal/5567248"),
        URL("https://www.daft.ie/for-sale/detached-house-12-f-na-rua-kells-road-kingscourt-co-cavan/5546081"),
        URL("https://www.daft.ie/for-sale/detached-house-castlecreevy-corrandulla-co-galway/5303535"),
        URL("https://www.daft.ie/for-sale/detached-house-lower-knockfola-derrybeg-co-donegal/5350177"),
        URL("https://www.daft.ie/for-sale/end-of-terrace-house-11-douglas-street-cork-city-co-cork/5492123"),
        URL("https://www.daft.ie/for-sale/detached-house-mellenton-house-carlow-town-co-carlow/4714472"),
        URL("https://www.daft.ie/for-sale/detached-house-16-the-gates-matthew-hill-lehenaghmore-co-cork/5498820"),
        URL("https://www.daft.ie/for-sale/terraced-house-67-douglas-street-cork-city-co-cork/5447655"),
        URL("https://www.daft.ie/for-sale/detached-house-tullycommons-castlebar-co-mayo/5408295"),
        URL("https://www.daft.ie/for-sale/detached-house-bushfield-loughrea-co-galway/5352064"),
        URL("https://www.daft.ie/for-sale/detached-house-residence-stable-yard-on-c-9-1-acres-kilmalum-blessington-co-wicklow/4716681"),
        URL("https://www.daft.ie/for-sale/detached-house-ballinanty-rathdrum-co-wicklow/5329664"),
        URL("https://www.daft.ie/for-sale/detached-house-the-beeches-kingswell-tipperary-town-co-tipperary/5646552"),
        URL("https://www.daft.ie/for-sale/apartment-apartment-6-block-d-butlers-court-dublin-2/5645812"),
        URL("https://www.daft.ie/for-sale/detached-house-carrowmoneen-tuam-co-galway/5647431"),
        URL("https://www.daft.ie/for-sale/detached-house-180-ballygall-road-east-glasnevin-dublin-11/5646697")
      )
    )
  }

  test("last page") {
    ScraperHelper.assertListPageScraperResults(
      "services/daftie/lists/last.html",
      "https://www.daft.ie/property-for-sale/ireland?from=13900",
      DaftIeListPageScraper,
      None,
      List(
        URL("https://www.daft.ie/for-sale/site-clugga-sixmilebridge-co-clare/11050"),
        URL("https://www.daft.ie/for-sale/site-cuilleenirwan-dysart-co-roscommon/20015"),
        URL("https://www.daft.ie/for-sale/site-lissane-clarecastle-co-clare/9442"),
        URL("https://www.daft.ie/for-sale/site-castleconway-killorglin-co-kerry/16154"),
        URL("https://www.daft.ie/for-sale/site-knockboy-kilmeena-westport-co-mayo/55066")
      )
    )
  }
}
