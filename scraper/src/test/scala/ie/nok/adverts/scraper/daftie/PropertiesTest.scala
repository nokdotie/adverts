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

    assertEquals(result.size, 20)
  }
}
