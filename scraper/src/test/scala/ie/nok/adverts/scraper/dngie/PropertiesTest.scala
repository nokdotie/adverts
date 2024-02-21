package ie.nok.adverts.scraper.dngie

import ie.nok.adverts.PropertyType
import ie.nok.adverts.services.dngie.DngIeAdvert
import ie.nok.zio.ZIOOps.unsafeRun
import munit.FunSuite
import zio.json.readJsonLinesAs

import scala.util.chaining.scalaUtilChainingOps

class PropertiesTest extends FunSuite {

  test("Parse properties") {
    val result =
      "scraper/src/test/resourses/dngie/properties.json"
        .pipe { readJsonLinesAs[Properties.Response](_) }
        .map { _.data.properties.flatten }
        .flattenIterables
        .map { v => Properties.toDngIeAdvert(v) }
        .map { DngIeAdvert.toAdvert }
        .runCollect
        .pipe { unsafeRun }
        .getOrElse { _ => fail("Unsafe run failed") }

    assertEquals(result.size, 100)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Site)), 56)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Apartment)), 1)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Bungalow)), 3)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Detached)), 12)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.SemiDetached)), 9)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Terraced)), 3)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.House)), 1)
    assertEquals(result.count(_.propertyType.isEmpty), 15)
  }
}
