package ie.nok.adverts.scraper.myhomeie

import ie.nok.adverts.PropertyType
import ie.nok.adverts.services.myhomeie.MyHomeIeAdvert
import ie.nok.zio.ZIOOps.unsafeRun
import munit.FunSuite
import zio.json.readJsonLinesAs

import scala.util.chaining.scalaUtilChainingOps

class PropertyTest extends FunSuite {

  test("Parse property - Detached") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/bedrock-giltspur-lane-southern-cross-road-bray-co-wicklow.json")

    assertEquals(result.address, "Bedrock Giltspur Lane, Southern Cross Road, Bray, Co. Wicklow")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Detached))
  }

  test("Parse property - Apartment") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/14-druid-s-court-druid-s-glen-newtownmountkennedy-co-wicklow.json")

    assertEquals(result.address, "14 Druid's Court, Druid's Glen, Newtownmountkennedy, Co. Wicklow")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Apartment))
  }

  test("Parse property - Bungalow") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/17-the-poplars-forest-park-portlaoise-laois.json")

    assertEquals(result.address, "17 The Poplars, Forest Park, Portlaoise, Laois")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Bungalow))
  }

  test("Parse property - Semi Detached") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/27-coolgreena-road-beaumont-dublin-9.json")

    assertEquals(result.address, "27, Coolgreena Road, Beaumont, Dublin 9")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.SemiDetached))
  }

  test("Parse property - Terraced") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/54-saint-mochta-s-green-clonsilla-dublin.json")

    assertEquals(result.address, "54 Saint Mochta's Green, Clonsilla, Dublin")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Terraced))
  }

  test("Parse property - End of Terrace") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/1-rosbeg-court-sutton-dublin-13.json")

    assertEquals(result.address, "1 Rosbeg Court, Sutton, Dublin 13")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.EndOfTerrace))
  }

  test("Parse property - Duplex") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/25-the-moyle-prospect-hill-finglas-dublin-11.json")

    assertEquals(result.address, "25 The Moyle, Prospect Hill, Finglas, Dublin 11")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Duplex))
  }

  test("Parse property - Studio") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/rd-na-glaise-stillorgan-county-dublin.json")

    assertEquals(result.address, "Árd Na Glaise, Stillorgan, County Dublin")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Studio))
  }

  test("Parse property - Site") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/site-at-illerton-killiney-hill-road-killiney-co-dublin.json")

    assertEquals(result.address, "Site At Illerton, Killiney Hill Road, Killiney, Co. Dublin")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Site))
  }

  test("Parse property - Country house") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/ballinacurra-house-kinsale-county-cork.json")

    assertEquals(result.address, "Ballinacurra House, Kinsale, County Cork")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.House))
  }

  test("Parse property - House") {
    val result = parseProperty("scraper/src/test/resourses/myhomeie/10-glebe-way-newcastle-co-dublin.json")

    assertEquals(result.address, "10 Glebe way, Newcastle, Co. Dublin")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.House))
  }

  private def parseProperty(resourcePath: String): MyHomeIeAdvert =
    resourcePath
      .pipe { readJsonLinesAs[Property.Response](_) }
      .runHead
      .map { _.get }
      .map { Property.toMyHomeIeAdvert(_, None) }
      .pipe { unsafeRun }
}
