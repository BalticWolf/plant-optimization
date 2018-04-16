package com.engineering.model

import com.engineering.pathmaker.ContextBuilder
import org.specs2.mutable.Specification
import org.specs2.specification.{AfterEach, BeforeEach}

class IndividualSpec extends Specification with BeforeEach with AfterEach {

  val controlIndividual: Individual = Individual().copy(entropy = 4256.21)

  override protected def before: Any = {
  }

  override protected def after: Any = {

  }

  "reset Individual" should {
    "-> only change its entropy" in {
      val isTested = controlIndividual.isTested
      val machines = controlIndividual.machines
      val nextIndividual = controlIndividual.reset()

      nextIndividual.entropy == 1000000.0 and
      nextIndividual.isTested == isTested and
      nextIndividual.machines == machines
    }

  }

  "whoIsBest" should {
    "-> yield the individual of minimum entropy among 2" in {
      val weakestIndividual = controlIndividual.copy(entropy = 9456.4)

      weakestIndividual.whoIsBest(controlIndividual) == controlIndividual
    }
  }

  "evaluate" should {
    "-> give this entropy" in {
      val controlIndividual = Individual(
        machines = List(0, 2, 3, 1, 3, 1, 2, 4, 1, 2, 0, 0, 4, 0, 3, 3, 0, 1, 1, 4, 2, 4, 3, 2, 4)
      )
      val traffic = Traffic(ContextBuilder.trafficMatrix)
      val evaluatedIndividual = controlIndividual.evaluate(traffic)

      evaluatedIndividual.entropy == 1577.9800000000014
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
