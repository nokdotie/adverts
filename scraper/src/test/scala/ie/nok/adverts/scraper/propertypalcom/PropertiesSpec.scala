package ie.nok.adverts.scraper.propertypalcom

import ie.nok.zio.ZIO
import ie.nok.unit.Area
import munit.FunSuite
import scala.util.chaining.scalaUtilChainingOps
import zio.json.readJsonLinesAs

class PropertySuite extends FunSuite {
  test("Parse property sizes in sq. feet") {
    val expected = BigDecimal(118.451325)
    val result =
      "scraper/src/test/resourses/propertypalcom/8-sea-breeze-heights-clonakilty.json"
        .pipe { readJsonLinesAs[Property.Response](_) }
        .map { _.pageProps.property }
        .map { Property.toPropertyPalComAdvert }
        .map { _.size }
        .collectSome
        .map { Area.toSquareMetres }
        .map { _.value }
        .runHead
        .someOrFailException
        .pipe { ZIO.unsafeRun(_) }
        .getOrElse { _ => fail("Missing property size") }

    assertEquals(expected, result)
  }

  test("Parse property sizes in sq. metres") {
    val expected = BigDecimal(271)
    val result =
      "scraper/src/test/resourses/propertypalcom/the-riggins-dunshaughlin.json"
        .pipe { readJsonLinesAs[Property.Response](_) }
        .map { _.pageProps.property }
        .map { Property.toPropertyPalComAdvert }
        .map { _.size }
        .collectSome
        .map { Area.toSquareMetres }
        .map { _.value }
        .runHead
        .someOrFailException
        .pipe { ZIO.unsafeRun(_) }
        .getOrElse { _ => fail("Missing property size") }

    assertEquals(expected, result)
  }

  test("Parse property sizes in acres") {
    val expected = BigDecimal(315655.08)
    val result =
      "scraper/src/test/resourses/propertypalcom/aughaweena-ardlougher-ballyconnell.json"
        .pipe { readJsonLinesAs[Property.Response](_) }
        .map { _.pageProps.property }
        .map { Property.toPropertyPalComAdvert }
        .map { _.size }
        .collectSome
        .map { Area.toSquareMetres }
        .map { _.value }
        .runHead
        .someOrFailException
        .pipe { ZIO.unsafeRun(_) }
        .getOrElse { _ => fail("Missing property size") }

    assertEquals(expected, result)
  }
}
