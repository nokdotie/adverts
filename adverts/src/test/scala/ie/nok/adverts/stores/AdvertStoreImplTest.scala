package ie.nok.adverts.stores

import ie.nok.adverts.{Advert, given}
import ie.nok.zio.ZIOOps.unsafeRun
import org.scalacheck.Prop._
import scala.util.chaining.scalaUtilChainingOps
import zio.http.Client
import zio.{ZIO, ZLayer}

class AdvertStoreImplTest extends munit.ScalaCheckSuite {
  test("Pagination works") {
    forAll { (one: Advert, two: Advert, three: Advert) =>
      val store = AdvertStoreImpl(List(one, two, three))

      val pageOne = store.getPage(AdvertFilter.Empty, 1, AdvertStoreCursor.Zero).pipe { unsafeRun }
      assertEquals(pageOne.items, List(one), "Page 1 - items")
      assertEquals(pageOne.pageInfo.hasNextPage, true, "Page 1 - hasNextPage")
      assertEquals(pageOne.pageInfo.hasPreviousPage, false, "Page 1 - hasPreviousPage")

      val pageTwo = store.getPage(AdvertFilter.Empty, 1, AdvertStoreCursor(1)).pipe { unsafeRun }
      assertEquals(pageTwo.items, List(two), "Page 2")
      assertEquals(pageTwo.pageInfo.hasNextPage, true, "Page 2 - hasNextPage")
      assertEquals(pageTwo.pageInfo.hasPreviousPage, true, "Page 2 - hasPreviousPage")

      val pageThree = store.getPage(AdvertFilter.Empty, 1, AdvertStoreCursor(2)).pipe { unsafeRun }
      assertEquals(pageThree.items, List(three), "Page 3")
      assertEquals(pageThree.pageInfo.hasNextPage, false, "Page 3 - hasNextPage")
      assertEquals(pageThree.pageInfo.hasPreviousPage, true, "Page 3 - hasPreviousPage")
    }
  }
}
