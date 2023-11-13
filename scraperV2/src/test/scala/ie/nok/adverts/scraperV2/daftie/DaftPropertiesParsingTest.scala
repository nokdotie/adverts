package ie.nok.adverts.scraperV2.daftie

import ie.nok.adverts.scraperV2.AdvertV2
import ie.nok.adverts.scraperV2.daftie.DaftPropertyResponseJsonDecoders.given_JsonDecoder_DaftResponse
import ie.nok.zio.ZIO
import munit.FunSuite
import zio.Chunk
import zio.json.readJsonLinesAs
import zio.stream.ZStream

import scala.util.chaining.scalaUtilChainingOps

class DaftPropertiesParsingTest extends FunSuite {

  private def inputStream(resourcePath: String): ZStream[Any, Throwable, DaftPropertyResponse.DaftResponse] = getClass
    .getResource(resourcePath)
    .pipe { readJsonLinesAs[DaftPropertyResponse.DaftResponse](_) }

  private def mappingTestResult(resourcePath: String): Chunk[AdvertV2] = inputStream(resourcePath)
    .mapConcat(DaftProperties.toAdvert)
    .runCollect
    .pipe { ZIO.unsafeRun }
    .getOrElse { cause => fail(s"Unsafe run failed: $cause.") }

  test("Parse daft properties from resources file") {
    val result: Chunk[AdvertV2] = mappingTestResult("/daftie/listing.json")

    // println(result.toList.mkString("\n"))
    assertEquals(result.size, 20)
  }

  test("Parse daft properties for Fran Grincell Properties file") {
    val result = mappingTestResult("/daftie/listing_Fran_Grincell_Properties.json")

    // println(result.toList.mkString("\n"))
    assertEquals(result.size, 20)
  }
}
