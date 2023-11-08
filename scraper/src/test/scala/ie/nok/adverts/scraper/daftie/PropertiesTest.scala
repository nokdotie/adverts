package ie.nok.adverts.scraper.daftie

import ie.nok.zio.ZIO
import munit.FunSuite
import scala.util.chaining.scalaUtilChainingOps
import zio.json.readJsonLinesAs

class PropertiesTest extends FunSuite {
  
  test("Parse properties") {
    val result =
      getClass.getResource("/daftie/listing.json")
        .pipe { readJsonLinesAs[Properties.Response](_) }
        .mapConcat { _.listings }
        .map { _.listing }
        .map { Properties.toDaftIeAdvert }
        .runCollect
        .pipe { ZIO.unsafeRun }
        .getOrElse { _ => fail("Unsafe run failed") }

    // println(result.toList.mkString("\n"))
    assertEquals(result.size, 20)
  }

}
