package ie.nok.adverts.scraper.daftie

import ie.nok.zio.ZIOOps.unsafeRun
import munit.FunSuite
import zio.http.Client
import zio.{ZIO, ZLayer}

import scala.util.chaining.scalaUtilChainingOps

class BuildIdTest extends FunSuite {
  test("Get buildId") {
    val latestBuildId =
      BuildId.latest
        .provide(Client.default)
        .pipe { unsafeRun }
        .getOrElse { _ => fail("Unsafe run failed") }

    println(latestBuildId)
    assert(latestBuildId.nonEmpty)
  }
}
