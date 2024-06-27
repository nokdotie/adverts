package ie.nok.adverts.stores

import ie.nok.adverts.{Advert, given}
import ie.nok.unit.Direction
import org.scalacheck.Prop._

class AdvertSortTest extends munit.ScalaCheckSuite {
  test("Sorting works") {
    forAll { (list: List[Advert], field: AdvertSortableField) =>
      val asc  = list.sorted(AdvertSort(field, Direction.Ascending).sort)
      val desc = list.sorted(AdvertSort(field, Direction.Descending).sort)

      assertEquals(asc, desc.reverse)
    }
  }
}
