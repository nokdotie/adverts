package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.adverts.PropertyType
import ie.nok.adverts.services.sherryfitzie.SherryFitzIeAdvert
import ie.nok.geographic.Coordinates
import ie.nok.unit.AreaUnit.Acres
import ie.nok.unit.{Area, AreaUnit}
import munit.FunSuite
import org.jsoup.Jsoup

import scala.io.Source

class PropertyTest extends FunSuite {

  test("Parse - Apartment") {
    val url    = "https://www.sherryfitz.ie/buy/apartment/dublin/tallaght/28-deerpark-place-kiltipper"
    val result = parseProperty("28-deerpark-place-kiltipper.html", url)

    assertEquals(result.address, "28 Deerpark Place, Kiltipper, Dublin 24")
    assert(result.description.nonEmpty)
    assertEquals(result.size, None)
    assertEquals(result.propertyType, Some(PropertyType.Apartment))
  }

  test("Parse - Bungalow") {
    val url    = "https://www.sherryfitz.ie/buy/bungalow/galway/moycullen/tawnybeg-moycullen"
    val result = parseProperty("tawnybeg-moycullen.html", url)

    assertEquals(result.address, "Tawnybeg, Moycullen, Co. Galway")
    assert(result.description.nonEmpty)
    assertEquals(result.size, Some(Area(111, AreaUnit.SquareMetres)))
    assertEquals(result.propertyType, Some(PropertyType.Bungalow))
  }

  test("Parse - Cottage") {
    val url    = "https://www.sherryfitz.ie/buy/cottage/mayo/knock/liscat-ballyhaunis"
    val result = parseProperty("liscat-ballyhaunis.html", url)

    assertEquals(result.address, "Liscat, Ballyhaunis,, Co.Mayo")
    assert(result.description.nonEmpty)
    assertEquals(result.size, Some(Area(0.5, AreaUnit.Acres)))
    assertEquals(result.propertyType, Some(PropertyType.House))
  }

  test("Parse - Country Homes") {
    val url    = "https://www.sherryfitz.ie/buy/country-homes/wexford/gorey/tearmann-tara-hill"
    val result = parseProperty("tearmann-tara-hill.html", url)

    assertEquals(result.address, "Tearmann, Tara Hill, Gorey, Co. Wexford")
    assert(result.description.nonEmpty)
    assertEquals(result.size, Some(Area(548, AreaUnit.Acres)))
    assertEquals(result.propertyType, Some(PropertyType.House))
  }

  test("Parse - Duplex") {
    val url    = "https://www.sherryfitz.ie/buy/duplex/wicklow/bray/47-roseville-court"
    val result = parseProperty("47-roseville-court.html", url)

    assertEquals(result.address, "47 Roseville Court, Dublin Road, Bray, Co. Wicklow")
    assert(result.description.nonEmpty)
    assertEquals(result.size, Some(Area(124, AreaUnit.SquareMetres)))
    assertEquals(result.propertyType, Some(PropertyType.Duplex))
  }

  test("Parse - House") {
    val url    = "https://www.sherryfitz.ie/buy/house/kildare/johnstown/29-furness-wood-johnstown"
    val result = parseProperty("29-furness-wood-johnstown.html", url)

    assertEquals(result.address, "29 Furness Wood,, Johnstown,, Co. Kildare")
    assert(result.description.nonEmpty)
    assertEquals(result.size, Some(Area(150, AreaUnit.SquareMetres)))
    assertEquals(result.propertyType, Some(PropertyType.House))
  }

  test("Parse - Land") {
    val url    = "https://www.sherryfitz.ie/buy/land/limerick/abbeyfeale/dromtrasna-collins"
    val result = parseProperty("dromtrasna-collins.html", url)
    assertEquals(result.address, "Dromtrasna Collins, Abbeyfeale, Co. Limerick")
    assert(result.description.nonEmpty)
    assertEquals(result.size, Some(Area(3.33, Acres)))
    assertEquals(result.propertyType, Some(PropertyType.Site))
  }

  test("Parse property - Detached House Size test") {
    val url    = "https://www.sherryfitz.ie/buy/house/dublin/shankill/3-the-sidings-station-road"
    val result = parseProperty("3-the-sidings-station-road.html", url)

    assertEquals(result.address, "3 The Sidings, Station Road, Shankill, Co.Dublin")
    assert(result.description.nonEmpty)
    assertEquals(result.size, Some(Area(167.2, AreaUnit.SquareMetres)))
    assertEquals(result.propertyType, Some(PropertyType.House))
  }

  private def parseProperty(fileName: String, url: String): SherryFitzIeAdvert = {
    // val source = Source.fromURL(url)
    val source = Source.fromFile(s"scraper/src/test/resourses/sherryfitzie/$fileName")
    val html   = source.getLines().mkString
    val doc    = Jsoup.parse(html)
    source.close()
    Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
  }
}
