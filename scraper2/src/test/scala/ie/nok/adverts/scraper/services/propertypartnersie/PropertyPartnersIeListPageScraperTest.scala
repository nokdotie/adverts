package ie.nok.adverts.scraper.services.propertypartnersie

import ie.nok.adverts.scraper.services.ScraperHelper
import java.net.URL

class PropertyPartnersIeListPageScraperTest extends munit.FunSuite {

  test("first page") {
    ScraperHelper.assertListPageScraperResults(
      "services/propertypartnersie/lists/first.html",
      PropertyPartnersIeListPageScraper.getFirstPageUrl().toString(),
      PropertyPartnersIeListPageScraper,
      Some(URL("https://www.propertypartners.ie/residential/results?page=2&status=2%7C3")),
      List(
        URL("https://www.propertypartners.ie/residential/brochure/21-holt-crescent-lugduff-tinahely-wicklow/4737247"),
        URL("https://www.propertypartners.ie/residential/brochure/37-derrybawn-aughrim-wicklow/4786112"),
        URL("https://www.propertypartners.ie/residential/brochure/apt-54-mountfield-park-seamount-road-malahide-dublin/4798365"),
        URL("https://www.propertypartners.ie/residential/brochure/8-meadow-court-dublin-rd-mullingar-westmeath/4798517"),
        URL("https://www.propertypartners.ie/residential/brochure/apt-64-lighthouse-apartments-church-road-east-wall-dublin-3/4798387"),
        URL("https://www.propertypartners.ie/residential/brochure/19-boyle-o-reilly-terrace-drogheda-louth/4788510"),
        URL("https://www.propertypartners.ie/residential/brochure/24-the-pines-forest-park-portlaoise-laois/4784188"),
        URL("https://www.propertypartners.ie/residential/brochure/27-crobally-heights-tramore-waterford/4798267"),
        URL("https://www.propertypartners.ie/residential/brochure/the-demesne-gowran-kilkenny/4630297"),
        URL("https://www.propertypartners.ie/residential/brochure/manusflynn-belclare-tuam-county-galway/4798029"),
        URL("https://www.propertypartners.ie/residential/brochure/tullig-killarney-county-kerry/4797979"),
        URL("https://www.propertypartners.ie/residential/brochure/22-the-rise-lakepoint-park-mullingar-westmeath/4797949")
      )
    )
  }

}
