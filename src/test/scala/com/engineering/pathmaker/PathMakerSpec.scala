package com.engineering.pathmaker

import com.engineering.pathmaker.ContextBuilder._
import org.specs2.mutable.Specification
import org.specs2.specification.{AfterEach, BeforeEach}


class PathMakerSpec extends Specification with BeforeEach with AfterEach {

  override protected def before: Any = {

  }

  override protected def after: Any = {

  }

  "Canonical environments" should {
    val controlProductMix = Array(
      Array(33.33, 33.33, 33.33),
      Array(25.00, 50.00, 25.00),
      Array(20.00, 40.00, 40.00),
      Array(25.00, 25.00, 50.00),
      Array(50.00, 25.00, 25.00),
      Array(40.00, 40.00, 20.00),
      Array(40.00, 20.00, 40.00)
    )

    val productMix = PathMaker.buildProductMix(Canonical.mask)

    "-> lead to canonical product mix" in {
      productMix.deep must beEqualTo(controlProductMix.deep)
    }

    "-> give this traffic matrix for environment 5" in {
      ok
    }
  }

  "Disrupted environments" should {
    val controlProductMix = Array(
      Array(36.17, 27.65, 36.17),
      Array(38.63, 38.63, 22.72),
      Array(39.53, 30.23, 30.23),
      Array(42.50, 25.00, 32.50),
      Array(38.63, 22.72, 38.63),
      Array(42.50, 32.50, 25.00),
      Array(36.17, 36.17, 27.65)
    )

    val productMix = PathMaker.buildProductMix(Disrupted.mask)

    "-> lead to disrupted product mix" in {
      productMix.deep must beEqualTo(controlProductMix.deep)
    }

    "-> give this traffic matrix for environment 2" in {
      ok
    }
  }
}
