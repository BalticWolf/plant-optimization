package com.engineering.model

import com.engineering.EnvironmentVariables.EnvironmentVariable.INDIVIDUALS_FOLDER_PATH
import com.engineering.utils.FileTools

import scala.util.Random


/**
  * The class Individual defines a plant configuration (machines evenly spread throughout cells).
  */
case class Individual(machines: List[Int],
                      entropy: Double,
                      isTested: Boolean) {

  /**
    * Evaluate entropy of the system, regarding the traffic matrix.
    * @param traffic matrix representing traffic between machine i and machine j
    * @return Individual with calculated entropy
    */
  def evaluate(traffic: Traffic): Individual = {
    val accumulator = for (
      i <- traffic.matrix.indices;
      j <- traffic.matrix.indices
      if i != j
    ) yield traffic.matrix(i)(j)

    this.copy(
      entropy = accumulator.sum,
      isTested = true
    )
  }

  /**
    * Mutate the current individual (shuffle machines).
    * @return mutated Individual
    */
  def mutate(): Individual = {
    this.copy(
      machines = this.swapMachines(this.machines),
      isTested = false
    )
  }

  /**
    * Compare entropy of two individuals.
    * @return stronger individual among this and that
    */
  def whoIsBest(that: Individual): Individual = {
    if (this.entropy >= that.entropy) that
    else this
  }

  /**
    * Randomly spread machines throughout cells.
    * @return shuffled list
    */
  private def swapMachines(machines: List[Int]): List[Int] = {
    Individual.shuffle(machines, Nil)
  }

  /**
    * Reset the current individual entropy.
    * @return copy of initial Individual with reset entropy
    */
  def reset(): Individual = this.copy(entropy = Individual.defaultEntropy)

  /**
    * Save a representation of the individual.
    * @param fileName is the name of the file in which the content will be saved
    */
  def writeToFile(fileName: String): Unit = {
    FileTools.getFolderPath(INDIVIDUALS_FOLDER_PATH) match {
      case Right(folder) =>
        FileTools.saveFileToFolder(folder, fileName, this.toString)
      case Left(error) => println(error)
    }
  }

  override def toString: String = {
    val composition = for (
      index <- 0 until Individual.nbCells
    ) yield (index, this.machines.zipWithIndex.filter(_._1 == index).map(_._2))

    "ENTROPY:\n" +
    entropy + "\n\n" +
    "LOCATION OF MACHINES:\n" +
    machines.mkString(" ") + "\n\n" +
    "COMPOSITION OF CELLS:\n" +
    composition.toMap.toString() + "\n"
  }
}

object Individual {
  val rand = new Random()

  val defaultEntropy = 1000000
  val minimumEntropy = 0
  private val nbCells: Int = 5
  private val cellSize: Int = 5

  def apply(): Individual = {
    new Individual(initialize(), defaultEntropy, false)
  }

  /**
    * Randomly spread machines throughout cells.
    */
  private def initialize(): List[Int] = {
    val installation = for (
      _ <- 0 until nbCells
    ) yield List.range(0, cellSize)

    shuffle(installation.flatten.toList, Nil)
  }

  /**
    * Shuffle list of Int.
    * @param before old list to deconstruct
    * @param after new list accumulator
    * @return shuffled list
    */
  private def shuffle(before: List[Int], after: List[Int]): List[Int] = before match {
    case Nil => after
    case remain =>
      val half = remain.splitAt(
        rand.nextInt(remain.length) // Select a random index in remain
      )

      shuffle(
        half._1 ++ half._2.tail, // exclude remain(i) from before
        after :+ half._2.head // append remain(i) to after
      )
  }
}
