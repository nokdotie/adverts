package ie.nok.adverts.stores

import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop._
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Random

class IntFilterSuite extends ScalaCheckSuite {
  property("Return true when equals") {
    forAll { (value: Int) =>
      IntFilter.Equals(value).filter(value)
    }
  }

  property("Return false when !equals") {
    forAll { (value: Int, filter: Int) =>
      value != filter ==>
        !IntFilter.Equals(filter).filter(value)
    }
  }

  property("Return true when greater than") {
    forAll { (value: Int, filter: Int) =>
      value > filter ==>
        IntFilter.GreaterThan(filter).filter(value)
    }
  }

  property("Return false when !greater than") {
    forAll { (value: Int, filter: Int) =>
      value <= filter ==>
        !IntFilter.GreaterThan(filter).filter(value)
    }
  }

  property("Return true when greater than or equal") {
    forAll { (value: Int, filter: Int) =>
      value >= filter ==>
        IntFilter.GreaterThanOrEqual(filter).filter(value)
    }
  }

  property("Return false when !greater than or equal") {
    forAll { (value: Int, filter: Int) =>
      value < filter ==>
        !IntFilter.GreaterThanOrEqual(filter).filter(value)
    }
  }

  property("Return true when less than") {
    forAll { (value: Int, filter: Int) =>
      value < filter ==>
        IntFilter.LessThan(filter).filter(value)
    }
  }

  property("Return false when !less than") {
    forAll { (value: Int, filter: Int) =>
      value >= filter ==>
        !IntFilter.LessThan(filter).filter(value)
    }
  }

  property("Return true when less than or equal") {
    forAll { (value: Int, filter: Int) =>
      value <= filter ==>
        IntFilter.LessThanOrEqual(filter).filter(value)
    }
  }

  property("Return false when !less than or equal") {
    forAll { (value: Int, filter: Int) =>
      value > filter ==>
        !IntFilter.LessThanOrEqual(filter).filter(value)
    }
  }

  property("Return true for empty") {
    forAll { (value: Int) =>
      IntFilter.Empty.filter(value)
    }
  }

  property("Return true when all ands are true") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      List
        .fill(size.abs)(IntFilter.Empty)
        .pipe { list => IntFilter.And(list.head, list.tail: _*) }
        .filter(value)
    }
  }

  property("Return false when one ands is false") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      !List
        .fill(size.abs)(IntFilter.Empty)
        .pipe { list => list :+ IntFilter.Equals(value + 1) }
        .pipe { Random.shuffle }
        .pipe { list => IntFilter.And(list.head, list.tail: _*) }
        .filter(value)
    }
  }

  property("Return true when one ors is true") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      List
        .fill(size.abs)(IntFilter.Equals(value + 1))
        .pipe { list => list :+ IntFilter.Empty }
        .pipe { Random.shuffle }
        .pipe { list => IntFilter.Or(list.head, list.tail: _*) }
        .filter(value)
    }
  }

  property("Return false when all ors are false") {
    forAll(arbitrary[Int], Gen.choose(1, 9)) { (value: Int, size: Int) =>
      !List
        .fill(size.abs)(IntFilter.Equals(value + 1))
        .pipe { list => IntFilter.Or(list.head, list.tail: _*) }
        .filter(value)
    }
  }
}
