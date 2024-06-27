package ie.nok.adverts.stores

import ie.nok.adverts.Advert
import java.time.Instant
import scala.math.Ordered.orderingToOrdered

enum AdvertCursor {
  case CreatedAt(createdAt: Instant, identifier: String)
  case Empty

}

object AdvertCursor {
  def from(advert: Advert, field: AdvertSortableField): AdvertCursor = field match {
    case AdvertSortableField.CreatedAt =>
      AdvertCursor.CreatedAt(advert.createdAt, advert.propertyIdentifier)
  }

  given Ordering[AdvertCursor] = new Ordering[AdvertCursor] {
    def compare(x: AdvertCursor, y: AdvertCursor): Int = (x, y) match {
      case (a: AdvertCursor.CreatedAt, b: AdvertCursor.CreatedAt) =>
        (a.createdAt, a.identifier).compare(b.createdAt, b.identifier)
      case _ => 0
    }
  }
}
