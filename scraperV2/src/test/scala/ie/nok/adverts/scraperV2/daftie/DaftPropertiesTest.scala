package ie.nok.adverts.scraperV2.daftie

import ie.nok.adverts.scraperV2.daftie.DaftPropertyResponseJsonDecoders.given_JsonDecoder_DaftResponse
import ie.nok.zio.ZIO
import munit.FunSuite
import zio.json.readJsonLinesAs

import scala.util.chaining.scalaUtilChainingOps

class DaftPropertiesTest extends FunSuite {

  test("Parse daft properties from resources file") {
    val result =
      getClass.getResource("/daftie/listing.json")
        .pipe { readJsonLinesAs[DaftPropertyResponse.DaftResponse](_) }
        .mapConcat { _.listings }
        .map { _.listing }
        .map { DaftProperties.toDaftIeAdvert }
        .runCollect
        .pipe { ZIO.unsafeRun }
        .getOrElse { cause => fail(s"Unsafe run failed: $cause.") }

    // println(result.toList.mkString("\n"))
    assertEquals(result.size, 20)
  }

  test("Parse daft properties for Fran Grincell Properties") {
    val result =
      getClass.getResource("/daftie/listing_Fran_Grincell_Properties.json")
        .pipe { readJsonLinesAs[DaftPropertyResponse.DaftResponse](_) }
        .mapConcat { _.listings }
        .map { _.listing }
        .map { DaftProperties.toDaftIeAdvert }
        .runCollect
        .pipe { ZIO.unsafeRun }
        .getOrElse { _ => fail("Unsafe run failed") }

    // println(result.toList.mkString("\n"))
    assertEquals(result.size, 20)
  }
}
