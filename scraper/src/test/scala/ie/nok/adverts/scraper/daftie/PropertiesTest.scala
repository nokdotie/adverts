package ie.nok.adverts.scraper.daftie

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

  test("Parse property") {
    val result =
      "scraper/src/test/resourses/daftie/property.json"
        .pipe { readJsonLinesAs[Property.Response](_) }
        .runHead
        .map { _.get }
        .map { Property.toDaftIeAdvert(_, None) }
        .pipe { unsafeRun }
        .getOrElse { _ => fail("Unsafe run failed") }

    assertEquals(result.address, "121 The Laurels, Tullow Road, Carlow Town, Co. Carlow")
  }
}
