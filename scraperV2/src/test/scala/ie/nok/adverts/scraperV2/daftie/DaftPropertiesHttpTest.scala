package ie.nok.adverts.scraperV2.daftie

import ie.nok.adverts.scraperV2.AdvertV2
import ie.nok.zio.ZIO
import munit.FunSuite
import zio.http.Client as ZioClient
import zio.stream.ZStream

import scala.util.chaining.scalaUtilChainingOps

class DaftPropertiesHttpTest extends FunSuite {

  private def httpStream(): ZStream[ZioClient, Throwable, AdvertV2] = DaftProperties
    .streamApiRequestContent(pageSize = 10)
    .mapZIO { DaftProperties.getApiResponse }
    .take(2) // take specific number of pages for testing
    .mapConcat(DaftProperties.toAdvert)

  test("Get daft properties") {
    val getAdverts =
      httpStream().runCollect
        .map(_.toList)
        .provide(ZioClient.default)

    val result = getAdverts
      .pipe { ZIO.unsafeRun }
      .getOrElse { cause => fail(s"Unsafe run failed: $cause.") }

    // println(result.mkString("\n"))
    assertEquals(result.size, 20)
  }
}
