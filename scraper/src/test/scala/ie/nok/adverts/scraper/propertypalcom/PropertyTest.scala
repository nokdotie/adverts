package ie.nok.adverts.scraper.propertypalcom

import ie.nok.adverts.PropertyType
import ie.nok.adverts.services.propertypalcom.PropertyPalComAdvert
import ie.nok.unit.AreaUnit.{Acres, SquareMetres}
import ie.nok.unit.{Area, AreaUnit}
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

  test("Parse description - Apartment") {
    val actual = parseProperty("scraper/src/test/resourses/propertypalcom/57-block-b-aras-na-cluaine-clondalkin-dublin.json").description
    assert(actual.exists(_.startsWith("""RAY COOKE AUCTIONEERS are delighted""".stripMargin)))
  }

  test("Parse description - EndOfTerrace") {
    val actual = parseProperty("scraper/src/test/resourses/propertypalcom/223a-eagle-valley-sarsfield-road-wilton-cork.json").description
    assert(actual.exists(_.startsWith("""Features
                                        |- Wilton
                                        |- Cork
                                        |- C1
                                        |Description""".stripMargin)))
  }

  test("Parse description - SemiDetached") {
    val actual = parseProperty("scraper/src/test/resourses/propertypalcom/8-glenside-ballycarnane-woods-tramore.json").description
    assert(actual.exists(_.startsWith(""".
                                        |
                                        |
                                        |
                                        |
                                        |Ground Floor:""".stripMargin)))
  }

  test("Parse property type - Apartment") {
    val expected = PropertyType.Apartment
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/57-block-b-aras-na-cluaine-clondalkin-dublin.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, Some(Area(57, SquareMetres)))
  }

  test("Parse property type - Bungalow") {
    val expected = PropertyType.Bungalow
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/shurock-castletown-geoghegan-mullingar.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, None)
  }

  test("Parse property type - Detached") {
    val expected = PropertyType.Detached
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/1-forest-view-rooskey-carrick-on-shannon.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, None)
  }

  test("Parse property type - SemiDetached") {
    val expected = PropertyType.SemiDetached
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/8-glenside-ballycarnane-woods-tramore.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, None)
  }

  test("Parse property type - Terraced") {
    val expected = PropertyType.Terraced
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/16-crosforge-saggart-dublin.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, Some(Area(166, SquareMetres)))
  }

  test("Parse property type - EndOfTerrace") {
    val expected = PropertyType.EndOfTerrace
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/223a-eagle-valley-sarsfield-road-wilton-cork.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, Some(Area(104, SquareMetres)))
  }

  test("Parse property type - Detached and Land") {
    val expected = PropertyType.Detached
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/rose-bank-derryginny-ballyconnell.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, Some(Area(175, SquareMetres)))
  }

  test("Parse property type - Bungalow and Land") {
    val expected = PropertyType.Bungalow
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/aughaweena-ardlougher-ballyconnell.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, Some(Area(78, Acres)))
  }

  test("Parse property type - House and Land") {
    val expected = PropertyType.House
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/ballykillowen-laghey.json")
    assertEquals(Option(expected), actual.propertyType)
    assertEquals(actual.size, Some(Area(98.42, SquareMetres)))
  }

  test("Parse description") {
    val expected = "Ballykillowen, Laghey, South Donegal F94 KXC2 Traditional Farm House on 3.65 Acres Requires Refurbishment"
    val actual   = parseProperty("scraper/src/test/resourses/propertypalcom/ballykillowen-laghey.json").description
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
}
