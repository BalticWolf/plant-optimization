package com.engineering.model

import scala.util.Random

case class Population(group: List[Individual],
                      bestGeneration: Int = 0) {

  /**
    * Test a population evolving through several generations.
    */
  def lifeCycle(traffic: Traffic, strongest: Individual): Population = {
    def loop(population: Population, countGeneration: Int): Population = {
      if (countGeneration < Population.maxGeneration) {
        val pop = mutate()
          .evaluate(traffic)
          .select(strongest, countGeneration)
        loop(pop, countGeneration + 1)
      }
      else population
    }
    loop(evaluate(traffic), 0)
  }

  /**
    * Evaluate all the Individuals in the population group.
    * @return evaluated population
    */
  def evaluate(traffic: Traffic): Population = {
    this.copy(
      group = this.group
        .filterNot(_.isTested)
        .map(_.evaluate(traffic))
    )
  }

  /**
    * Make some individuals randomly mutate in the population.
    * @return population with some mutated individuals
    */
  def mutate(): Population = {
    def loop(before: List[Individual], after: List[Individual]): List[Individual] = before match {
      case Nil => after
      case remain =>
        val p = Population.rand.nextInt(100) / 100F
        val nextIndividual =
          if (p > 1 - Population.mutationProbability)
            remain.head.mutate()
          else
            remain.head

        loop(remain.tail, after :+ nextIndividual)
    }
    this.copy(
      loop(group.tail, Nil) // exclude the best individual, which is head
    )
  }

  /**
    * Select some of the best individuals.
    * @return population having some of the best individuals
    */
  def select(strongestOfAllTime: Individual, currentGeneration: Int): Population = {
    val strongestOfGroup = getBestIndividual(strongestOfAllTime :: this.group)

    val bestGeneration = if (strongestOfGroup != strongestOfAllTime) currentGeneration else this.bestGeneration

    def loop(before: List[Individual], after: List[Individual]): List[Individual] = before match {
      case Nil => after
      case remain =>
        val index = Population.rand.nextInt(remain.length)
        val localStrongest = remain(index).whoIsBest(remain.head)
        loop(remain.tail, after :+ localStrongest)
    }
    this.copy(
      group = loop(this.group.tail, Nil),
      bestGeneration = bestGeneration
    )
  }

  /**
    * Get the individual having the minimum entropy, among a given list.
    * @param pop list of individuals
    * @return best individual regarding its entropy
    */
  def getBestIndividual(pop: List[Individual]): Individual = pop.minBy(_.entropy)
}

object Population {
  val rand: Random = new Random()

  private val maxPopulationSize: Int = 30
  private val mutationProbability: Float = 0.5F
  private val maxGeneration: Int = 1000

  def apply(): Population = new Population(populate())

  /**
    * Generate a list of individuals.
    */
  private def populate(): List[Individual] = {
    val group = for {
      _ <- 0 until maxPopulationSize
    } yield {
      Individual()
    }
    group.toList
  }
}
