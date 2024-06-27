package ie.nok.adverts.stores

import ie.nok.adverts.{Advert, given}
import ie.nok.unit.Direction
import ie.nok.zio.ZIOOps.unsafeRun
import org.scalacheck.Prop._
import scala.util.chaining.scalaUtilChainingOps
import zio.http.Client
import zio.{ZIO, ZLayer}

class AdvertStoreImplTest extends munit.ScalaCheckSuite {
  test("Pagination works") {
    forAll { (one: Advert, two: Advert, three: Advert, sort: AdvertSort) =>
      val list = List(one, two, three)
      val listSortedWithCursor = list
        .sorted(sort.sort)
        .map { advert => (advert, AdvertCursor.from(advert, sort.field)) }

      val store = AdvertStoreImpl(list)

      val pageOne = store.getPage(AdvertFilter.Empty, sort, 1, AdvertCursor.Empty).pipe { unsafeRun }
      assertEquals(pageOne.items, List(listSortedWithCursor(0)), "Page 1 - items")
      assertEquals(pageOne.pageInfo.hasNextPage, true, "Page 1 - hasNextPage")
      assertEquals(pageOne.pageInfo.hasPreviousPage, false, "Page 1 - hasPreviousPage")

      val pageTwo = store.getPage(AdvertFilter.Empty, sort, 1, listSortedWithCursor(0)._2).pipe { unsafeRun }
      assertEquals(pageTwo.items, List(listSortedWithCursor(1)), "Page 2")
      assertEquals(pageTwo.pageInfo.hasNextPage, true, "Page 2 - hasNextPage")
      assertEquals(pageTwo.pageInfo.hasPreviousPage, true, "Page 2 - hasPreviousPage")

      val pageThree = store.getPage(AdvertFilter.Empty, sort, 1, listSortedWithCursor(1)._2).pipe { unsafeRun }
      assertEquals(pageThree.items, List(listSortedWithCursor(2)), "Page 3")
      assertEquals(pageThree.pageInfo.hasNextPage, false, "Page 3 - hasNextPage")
      assertEquals(pageThree.pageInfo.hasPreviousPage, true, "Page 3 - hasPreviousPage")
    }
  }
}
