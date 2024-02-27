package ie.nok.adverts.scraper.sherryfitzie

import ie.nok.adverts.PropertyType
import ie.nok.geographic.Coordinates
import munit.FunSuite
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class PropertyTest extends FunSuite {

  test("Parse - Apartment") {
    val url           = "https://www.sherryfitz.ie/buy/apartment/dublin/tallaght/28-deerpark-place-kiltipper"
    val doc: Document = Jsoup.connect(url).get()
    val result        = Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
    assert(result.propertyType.contains(PropertyType.Apartment))
  }

  test("Parse - Bungalow") {
    val url           = "https://www.sherryfitz.ie/buy/bungalow/galway/moycullen/tawnybeg-moycullen"
    val doc: Document = Jsoup.connect(url).get()
    val result        = Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
    assert(result.propertyType.contains(PropertyType.Bungalow))
  }

  test("Parse - Cottage") {
    val url           = "https://www.sherryfitz.ie/buy/cottage/mayo/knock/liscat-ballyhaunis"
    val doc: Document = Jsoup.connect(url).get()
    val result        = Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
    assert(result.propertyType.contains(PropertyType.House))
  }

  test("Parse - County Homes") {
    val url           = "https://www.sherryfitz.ie/buy/country-homes/wexford/gorey/tearmann-tara-hill"
    val doc: Document = Jsoup.connect(url).get()
    val result        = Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
    assert(result.propertyType.contains(PropertyType.House))
  }

  test("Parse - Duplex") {
    val url           = "https://www.sherryfitz.ie/buy/duplex/wicklow/bray/47-roseville-court"
    val doc: Document = Jsoup.connect(url).get()
    val result        = Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
    assert(result.propertyType.contains(PropertyType.Duplex))
  }

  test("Parse - House") {
    val url           = "https://www.sherryfitz.ie/buy/house/kildare/johnstown/29-furness-wood-johnstown"
    val doc: Document = Jsoup.connect(url).get()
    val result        = Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
    assert(result.propertyType.contains(PropertyType.House))
  }

  test("Parse - Land") {
    val url           = "https://www.sherryfitz.ie/buy/land/limerick/abbeyfeale/dromtrasna-collins"
    val doc: Document = Jsoup.connect(url).get()
    val result        = Property.toSherryFitzIeAdvert(url, Coordinates.zero, doc)
    assert(result.propertyType.contains(PropertyType.Site))
  }
}
