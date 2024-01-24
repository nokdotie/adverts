package ie.nok.adverts.scraper.daftie

import ie.nok.adverts.PropertyType
import ie.nok.adverts.services.daftie.DaftIeAdvert
import ie.nok.zio.ZIOOps.unsafeRun
import munit.FunSuite
import zio.json.readJsonLinesAs

import scala.util.chaining.scalaUtilChainingOps

class PropertiesTest extends FunSuite {
  test("Parse properties") {
    val result =
      "scraper/src/test/resourses/daftie/listing.json"
        .pipe { readJsonLinesAs[Properties.Response](_) }
        .mapConcat { _.listings }
        .map { _.listing.id }
        .runCollect
        .pipe { unsafeRun }
        .getOrElse { _ => fail("Unsafe run failed") }

    assertEquals(result.size, 20)
  }

  test("Parse property - Apartment") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_apartment.json")

    assertEquals(result.address, "16 Elm Park, Coolcotts, Wexford Town, Co. Wexford")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Apartment))
  }

  test("Parse property - Bungalow") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_bungalow.json")

    assertEquals(result.address, "Fortnaven View, Ballindrumlea, Castlerea, Co. Roscommon")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Bungalow))
  }

  test("Parse property - Detached") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_detached.json")

    assertEquals(result.address, "Facefield, Ballynamaul, Kinsale, Co. Cork")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Detached))
  }

  test("Parse property - Duplex") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_duplex.json")

    assertEquals(result.address, "Three Bedroom Town House, Arcadin, Arcadin, Athlone, Co. Westmeath")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Duplex))
  }

  test("Parse property - End of Terrace") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_end_of_tarrace.json")

    assertEquals(result.address, "121 The Laurels, Tullow Road, Carlow Town, Co. Carlow")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.EndOfTerrace))
  }

  test("Parse property - Semi D") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_semi_d.json")

    assertEquals(result.address, "22 Dorrins Strand, Strandhill, Co. Sligo")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.SemiDetached))
  }

  test("Parse property - Site") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_site.json")

    assertEquals(result.address, "Cloondace, Knock, Co. Mayo")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Site))
  }

  test("Parse property - Terraced") {
    val result = parseProperty("scraper/src/test/resourses/daftie/property_terraced.json")

    assertEquals(result.address, "9B Liam Mellows Street, Tuam, Co. Galway")
    assert(result.description.nonEmpty)
    assertEquals(result.propertyType, Some(PropertyType.Terraced))
  }

  private def parseProperty(resourcePath: String): DaftIeAdvert =
    resourcePath
      .pipe { readJsonLinesAs[Property.Response](_) }
      .runHead
      .map { _.get }
      .map { Property.toDaftIeAdvert(_, None) }
      .pipe { unsafeRun }
      .getOrElse { _ => fail("Unsafe run failed") }
}
