package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import ie.nok.unit.{Direction, given}
import java.time.Instant
import scala.util.chaining.scalaUtilChainingOps
import zio.json.{DeriveJsonCodec, JsonCodec}

enum AdvertSortableField {
  case CreatedAt
}

object AdvertSortableField {
  given JsonCodec[AdvertSortableField] = DeriveJsonCodec.gen[AdvertSortableField]
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

object AdvertSort {
  val default = AdvertSort(AdvertSortableField.CreatedAt, Direction.Descending)

  given JsonCodec[AdvertSort] = DeriveJsonCodec.gen[AdvertSort]
}
