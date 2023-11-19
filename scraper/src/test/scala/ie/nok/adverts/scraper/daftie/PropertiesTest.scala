package ie.nok.adverts.scraper.daftie

import ie.nok.zio.ZIO
import munit.FunSuite
import zio.json.readJsonLinesAs

import scala.util.chaining.scalaUtilChainingOps

class PropertiesTest extends FunSuite {
  test("Parse properties") {
    val result =
      "scraper/src/test/resourses/daftie/listing.json"
        .pipe { readJsonLinesAs[Properties.Response](_) }
        .mapConcat { _.listings }
        .map { _.listing }
        .map { Properties.toDaftIeAdvert(_, None) }
        .runCollect
        .pipe { ZIO.unsafeRun }
        .getOrElse { _ => fail("Unsafe run failed") }

    assertEquals(result.size, 20)
  }

}
