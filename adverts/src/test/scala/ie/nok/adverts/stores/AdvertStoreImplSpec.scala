package ie.nok.adverts.stores

import ie.nok.adverts._
import ie.nok.zio.ZIO
import munit.ScalaCheckSuite
import org.scalacheck.Prop._
import scala.util.chaining.scalaUtilChainingOps

class AdvertStoreImplSuite extends ScalaCheckSuite {
  property("Return all adverts") {
    forAll { (adverts: List[Advert]) =>
      val store = new AdvertStoreImpl(adverts)
      val result = store
        .getPage(AdvertFilter.Empty, adverts.size, AdvertStoreCursor(0))
        .pipe(ZIO.unsafeRun)
        .toEither
        .fold(_ => 0, _.size)

      assertEquals(adverts.size, result)
    }
  }

  property("Return adverts where price is greater than threshold") {
    forAll { (adverts: List[Advert], threshold: Int) =>
      val store = new AdvertStoreImpl(adverts)
      val result = store
        .getPage(
          AdvertFilter.PriceInEur(NumericFilter.GreaterThan(threshold)),
          adverts.size,
          AdvertStoreCursor(0)
        )
        .pipe(ZIO.unsafeRun)
        .toEither
        .fold(_ => 0, _.size)

      val expected = adverts.count { _.advertPriceInEur > threshold }

      assertEquals(expected, result)
    }
  }
}
