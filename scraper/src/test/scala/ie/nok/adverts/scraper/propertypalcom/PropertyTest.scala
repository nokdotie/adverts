package ie.nok.adverts.scraper.propertypalcom

import ie.nok.adverts.PropertyType
import ie.nok.adverts.services.propertypalcom.PropertyPalComAdvert
import ie.nok.unit.Area
import ie.nok.zio.ZIOOps.unsafeRun
import munit.FunSuite
import zio.json.readJsonLinesAs

import scala.util.chaining.scalaUtilChainingOps

class PropertyTest extends FunSuite {

  test("Parse property sizes in sq. feet") {
    val expected = BigDecimal(118.451325)
    val actual = parseProperty("scraper/src/test/resourses/propertypalcom/8-sea-breeze-heights-clonakilty.json").size
      .map { Area.toSquareMetres }
      .map { _.value }
    assertEquals(Option(expected), actual)
  }

  test("Parse property sizes in sq. metres") {
    val expected = BigDecimal(271)
    val actual = parseProperty("scraper/src/test/resourses/propertypalcom/the-riggins-dunshaughlin.json").size
      .map { Area.toSquareMetres }
      .map { _.value }
    assertEquals(Option(expected), actual)
  }

  test("Parse property sizes in acres") {
    val expected = BigDecimal(315655.08)
    val actual = parseProperty("scraper/src/test/resourses/propertypalcom/aughaweena-ardlougher-ballyconnell.json").size
      .map { Area.toSquareMetres }
      .map { _.value }
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - Apartment") {
    val expected = PropertyType.Apartment
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/57-block-b-aras-na-cluaine-clondalkin-dublin.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - Bungalow") {
    val expected = PropertyType.Bungalow
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/shurock-castletown-geoghegan-mullingar.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - Detached") {
    val expected = PropertyType.Detached
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/1-forest-view-rooskey-carrick-on-shannon.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - SemiDetached") {
    val expected = PropertyType.SemiDetached
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/8-glenside-ballycarnane-woods-tramore.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - Terraced") {
    val expected = PropertyType.Terraced
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/16-crosforge-saggart-dublin.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - EndOfTerrace") {
    val expected = PropertyType.EndOfTerrace
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/223a-eagle-valley-sarsfield-road-wilton-cork.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - Detached and Land") {
    val expected = PropertyType.Detached
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/rose-bank-derryginny-ballyconnell.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - Bungalow and Land") {
    val expected = PropertyType.Bungalow
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/aughaweena-ardlougher-ballyconnell.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse property type - House and Land") {
    val expected = PropertyType.House
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/ballykillowen-laghey.json").propertyType
    assertEquals(Option(expected), actual)
  }

  test("Parse description") {
    val expected =
      "<h3>Ballykillowen, \u00a0Laghey,\u00a0South Donegal\u00a0F94 KXC2</h3><h3>\u00a0</h3><h3>Traditional Farm House on\u00a03.65\u00a0Acres</h3>"
    val actual = parseProperty("scraper/src/test/resourses/propertypalcom/ballykillowen-laghey.json").description
    assert(actual.exists(_.startsWith(expected)))
  }

  private def parseProperty(resourcePath: String): PropertyPalComAdvert =
    resourcePath
      .pipe { readJsonLinesAs[Property.Response](_) }
      .runHead
      .map { _.get }
      .map { Property.toPropertyPalComAdvertOption(_, None) }
      .someOrFailException
      .pipe { unsafeRun }
      .getOrElse { _ => fail("Unsafe run failed") }
}
