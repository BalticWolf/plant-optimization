package com.engineering.model

import org.specs2.mutable.Specification
import org.specs2.specification.{AfterEach, BeforeEach}

class IndividualTest extends Specification with BeforeEach with AfterEach {

  val testIndividual: Individual = Individual().copy(entropy = 4256.21)

  override protected def before: Any = {
  }

  override protected def after: Any = {

  }

  "reset Individual" should {

    "-> only change its entropy" in {
      val isTested = testIndividual.isTested
      val machines = testIndividual.machines
      val nextIndividual = testIndividual.reset()

      nextIndividual.entropy == 1000000.0 and
      nextIndividual.isTested == isTested and
      nextIndividual.machines == machines
    }

  }

  "whoIsBest" should {
    "-> yield the individual of minimum entropy among 2" in {
      val weakestIndividual = testIndividual.copy(entropy = 9456.4)

      weakestIndividual.whoIsBest(testIndividual) == testIndividual
    }
  }

  "mutate Individual" should {
    val controlIndividual = Individual(
      machines = List(0, 3, 0, 0, 1, 1, 3, 4, 1, 2, 4, 4, 1, 2, 0, 2, 2, 2, 4, 4, 0, 3, 3, 1, 3),
      entropy = 2566.12,
      isTested = true)
    val mutatedIndividual = controlIndividual.mutate()

    "-> change its machines configuration" in {
      controlIndividual.machines must not be equalTo(mutatedIndividual.machines)
    }

    "-> reset isTested to false" in {
      mutatedIndividual.isTested must beFalse
    }
  }
}
