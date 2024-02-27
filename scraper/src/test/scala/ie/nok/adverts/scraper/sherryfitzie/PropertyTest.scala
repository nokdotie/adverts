package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.adverts.PropertyType
import ie.nok.adverts.services.sherryfitzie.SherryFitzIeAdvert
import ie.nok.geographic.Coordinates
import munit.FunSuite
import org.jsoup.Jsoup

import scala.io.Source

class PropertyTest extends FunSuite {

  test("Parse - Apartment") {
    val url    = "https://www.sherryfitz.ie/buy/apartment/dublin/tallaght/28-deerpark-place-kiltipper"
    val result = parseProperty("28-deerpark-place-kiltipper.html", url)
    assert(result.propertyType.contains(PropertyType.Apartment))
  }

  test("Parse - Bungalow") {
    val url    = "https://www.sherryfitz.ie/buy/bungalow/galway/moycullen/tawnybeg-moycullen"
    val result = parseProperty("tawnybeg-moycullen.html", url)
    assert(result.propertyType.contains(PropertyType.Bungalow))
  }

  test("Parse - Cottage") {
    val url    = "https://www.sherryfitz.ie/buy/cottage/mayo/knock/liscat-ballyhaunis"
    val result = parseProperty("liscat-ballyhaunis.html", url)
    assert(result.propertyType.contains(PropertyType.House))
  }

  test("Parse - County Homes") {
    val url    = "https://www.sherryfitz.ie/buy/country-homes/wexford/gorey/tearmann-tara-hill"
    val result = parseProperty("tearmann-tara-hill.html", url)
    assert(result.propertyType.contains(PropertyType.House))
  }

  test("Parse - Duplex") {
    val url    = "https://www.sherryfitz.ie/buy/duplex/wicklow/bray/47-roseville-court"
    val result = parseProperty("47-roseville-court.html", url)
    assert(result.propertyType.contains(PropertyType.Duplex))
  }

  test("Parse - House") {
    val url    = "https://www.sherryfitz.ie/buy/house/kildare/johnstown/29-furness-wood-johnstown"
    val result = parseProperty("29-furness-wood-johnstown.html", url)
    assert(result.propertyType.contains(PropertyType.House))
  }

  test("Parse - Land") {
    val url    = "https://www.sherryfitz.ie/buy/land/limerick/abbeyfeale/dromtrasna-collins"
    val result = parseProperty("dromtrasna-collins.html", url)
    assert(result.propertyType.contains(PropertyType.Site))
  }

  private def parseProperty(fileName: String, url: String): SherryFitzIeAdvert = {
    val source = Source.fromFile(s"scraper/src/test/resourses/sherryfitzie/$fileName")
    val html   = source.getLines().mkString
    val doc    = Jsoup.parse(html)
    source.close()
    Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
  }
}
