package ie.nok.adverts.stores

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop._
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Random

class NumericFilterSuite extends ScalaCheckSuite {
  property("Return true when equals") {
    forAll { (value: Int) =>
      NumericFilter.Equals(value).filter(value)
    }
  }

  property("Return false when !equals") {
    forAll { (value: Int, filter: Int) => value != filter ==>
      !NumericFilter.Equals(filter).filter(value)
    }
  }

  property("Return true when greater than") {
    forAll { (value: Int, filter: Int) => value > filter ==>
      NumericFilter.GreaterThan(filter).filter(value)
    }
  }

  property("Return false when !greater than") {
    forAll { (value: Int, filter: Int) => value <= filter ==>
      !NumericFilter.GreaterThan(filter).filter(value)
    }
  }

  property("Return true when greater than or equal") {
    forAll { (value: Int, filter: Int) => value >= filter ==>
      NumericFilter.GreaterThanOrEqual(filter).filter(value)
    }
  }

  property("Return false when !greater than or equal") {
    forAll { (value: Int, filter: Int) => value < filter ==>
      !NumericFilter.GreaterThanOrEqual(filter).filter(value)
    }
  }

  property("Return true when less than") {
    forAll {  (value: Int, filter: Int) => value < filter ==>
      NumericFilter.LessThan(filter).filter(value)
    }
  }

  property("Return false when !less than") {
    forAll {  (value: Int, filter: Int) => value >= filter ==>
      !NumericFilter.LessThan(filter).filter(value)
    }
  }

  property("Return true when less than or equal") {
    forAll {  (value: Int, filter: Int) => value <= filter ==>
      NumericFilter.LessThanOrEqual(filter).filter(value)
    }
  }

  property("Return false when !less than or equal") {
    forAll {  (value: Int, filter: Int) => value > filter ==>
      !NumericFilter.LessThanOrEqual(filter).filter(value)
    }
  }

  property("Return true for empty") {
    forAll { (value: Int) =>
      NumericFilter.Empty[Int]().filter(value)
    }
  }

  property("Return true when all ands are true") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      List.fill(size.abs)(NumericFilter.Empty[Int]())
        .pipe { list => NumericFilter.And[Int](list.head, list.tail: _*) }
        .filter(value)
    }
  }

  property("Return false when one ands is false") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      !List.fill(size.abs)(NumericFilter.Empty[Int]())
        .pipe { list => list :+ NumericFilter.Equals(value + 1) }
        .pipe { Random.shuffle }
        .pipe { list => NumericFilter.And[Int](list.head, list.tail: _*) }
        .filter(value)
    }
  }

  property("Return true when one ors is true") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      List.fill(size.abs)(NumericFilter.Equals(value + 1))
        .pipe { list => list :+ NumericFilter.Empty[Int]() }
        .pipe { Random.shuffle }
        .pipe { list => NumericFilter.Or[Int](list.head, list.tail: _*) }
        .filter(value)
    }
  }

  property("Return false when all ors are false") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      !List.fill(size.abs)(NumericFilter.Equals(value + 1))
        .pipe { list => NumericFilter.Or[Int](list.head, list.tail: _*) }
        .filter(value)
    }
  }
}
