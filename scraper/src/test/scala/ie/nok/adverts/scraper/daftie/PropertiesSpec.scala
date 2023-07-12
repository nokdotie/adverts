package ie.nok.adverts.scraper.daftie

import ie.nok.zio.ZIO
import munit.FunSuite
import scala.util.chaining.scalaUtilChainingOps
import zio.json.readJsonLinesAs

class PropertySuite extends FunSuite {
  test("Parse properties") {
    val result =
      "scraper/src/test/resourses/daftie/listing.json"
        .pipe { readJsonLinesAs[Properties.Response](_) }
        .mapConcat { _.listings }
        .map { _.listing }
        .map { Properties.toAdvert }
        .runCollect
        .pipe { ZIO.unsafeRun(_) }
        .getOrElse { _ => fail("Unsafe run failed") }

    assert(result.nonEmpty)
  }

}
