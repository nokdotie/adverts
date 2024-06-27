package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.unit.Direction
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps

enum AdvertSortableField {
  case CreatedAt
}

case class AdvertSort(
    field: AdvertSortableField,
    direction: Direction
) {
  val sort: Ordering[Advert] = field
    .pipe { case AdvertSortableField.CreatedAt =>
      Ordering.by[Advert, (Instant, String)] { advert =>
        (advert.createdAt, advert.propertyIdentifier)
      }
    }
    .pipe { ordering =>
      direction match
        case Direction.Ascending  => ordering
        case Direction.Descending => ordering.reverse
    }
}
