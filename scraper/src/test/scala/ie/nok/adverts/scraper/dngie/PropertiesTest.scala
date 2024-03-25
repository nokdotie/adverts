package ie.nok.adverts.scraper.dngie

import ie.nok.adverts.services.dngie.DngIeAdvert
import ie.nok.adverts.{Advert, PropertyType}
import ie.nok.unit.Area
import ie.nok.unit.AreaUnit.{Acres, Hectares, SquareFeet, SquareMetres}
import ie.nok.zio.ZIOOps.unsafeRun
import munit.FunSuite
import zio.json.readJsonLinesAs

import scala.util.chaining.scalaUtilChainingOps

class PropertiesTest extends FunSuite {

  private lazy val result: List[Advert] =
    "scraper/src/test/resourses/dngie/properties.json"
      .pipe { readJsonLinesAs[Properties.Response](_) }
      .map { _.data.properties.flatten }
      .flattenIterables
      .map { v => Properties.toDngIeAdvert(v) }
      .map { DngIeAdvert.toAdvert }
      .runCollect
      .pipe { unsafeRun }
      .toList

  test("Parse properties count per propertyType") {
    assertEquals(result.size, 100)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Site)), 56)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Apartment)), 1)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Bungalow)), 3)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Detached)), 12)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.SemiDetached)), 9)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.Terraced)), 3)
    assertEquals(result.count(_.propertyType.exists(_ == PropertyType.House)), 1)
    assertEquals(result.count(_.propertyType.isEmpty), 15)

  }

  test("Parse property size - Acres") {
    val actualAcres: Option[Advert] = result.find(_.advertUrl == "https://dng.q.starberry.com/property-sale/4388229")
    assertEquals(actualAcres.map(_.propertySize), Some(Area(0.83, Acres)))
  }

  test("Parse property size - Hectares") {
    val actualHectares: Option[Advert] = result.find(_.advertUrl == "https://dng.q.starberry.com/property-sale/4625484")
    assertEquals(actualHectares.map(_.propertySize), Some(Area(0.23, Hectares)))
  }

  test("Parse property size - None") {
    val actualNone: Option[Advert] = result.find(_.advertUrl == "https://www.dng.ie/property-sale/4495805")
    assertEquals(actualNone.map(_.propertySize), Some(Area.zero))
  }

  test("Parse property size - SquareMetres") {
    val actualSqMeters: Option[Advert] = result.find(_.advertUrl == "https://dng.q.starberry.com/property-sale/4573586")
    assertEquals(actualSqMeters.map(_.propertySize), Some(Area(150.77, SquareMetres)))
  }

  test("Parse property size - SquareFeet") {
    val actualSqMeters: Option[Advert] = result.find(_.advertUrl == "https://dng.q.starberry.com/property-sale/4458544")
    assertEquals(actualSqMeters.map(_.propertySize), Some(Area(2650, SquareFeet)))
  }
}
