package com.engineering.optimizer

import java.io.File

import com.engineering.EnvironmentVariables
import com.engineering.EnvironmentVariables.EnvironmentVariable.{REPLICATIONS, TRAFFIC_FOLDER_PATH}
import com.engineering.model.{Individual, Population, Traffic}
import com.engineering.utils.FileTools

import scala.io.Source
import scala.util.{Failure, Success, Try}


object Optimizer extends App {

  /**
    * Main method.
    */
  override def main(args: Array[String]): Unit = {
    println("Optimizer just started...")

    EnvironmentVariables.envOrError(REPLICATIONS) match {
      case Right(replications) =>
        FileTools.listFiles(TRAFFIC_FOLDER_PATH) match {
          case Right(files) =>
            files.foreach(file => {
              Try(replications.toInt) match {
                case Success(rep) =>

                  val replicationResults = testTraffic(file, rep)

                  println("\nAverage entropy of the strongest: " + replicationResults.totalBestEntropy / rep)
                  println("\nAverage generation of the strongest: " + replicationResults.totalBestGeneration / rep)
                  println("\nBest entropy of all time: " + replicationResults.allTimeBestEntropy)
                  println("\nWorst entropy of all time: "+ replicationResults.allTimeWorstEntropy)

                case Failure(error) => println(error)
              }
            })
          case Left(error) => println(error)
        }
      case Left(error) => println(error)
    }
    println("\nFinished")
  }

  /**
    * For a given traffic file, test a population in evolution, on several replications.
    * @param file traffic input
    * @param replications number of population life cycles
    * @return quadruplet of simulation results
    */
  private def testTraffic(file: File, replications: Int): ReplicationResults = {
    val resultsFileName: String = "RES_" + file.getName
    val traffic = readTraffic(file)

    val initialPopulation = Population() // creation of the primordial population
    val initialIndividual = Individual() // creation of the primordial control individual

    val replicationStarters = ReplicationResults(0, 0, Individual.defaultEntropy, Individual.minimumEntropy)

    def loop(replications: Int,
             population: Population,
             strongest: Individual,
             replicationResults: ReplicationResults): ReplicationResults = replications match {
      case r if r > 0 =>

        val nextPop = population.lifeCycle(traffic, strongest)
        val nextStrongest = nextPop.getBestIndividual(nextPop.group)

        val reAssessedEntropy = nextStrongest.evaluate(traffic).entropy
        nextStrongest.writeToFile(resultsFileName)

        val nextResults = ReplicationResults(
          replicationResults.totalBestEntropy + reAssessedEntropy,
          replicationResults.totalBestGeneration + population.bestGeneration,
          math.min(reAssessedEntropy, replicationResults.allTimeBestEntropy),
          math.max(reAssessedEntropy, replicationResults.allTimeWorstEntropy)
        )

        loop(replications - 1, nextPop, nextStrongest.reset(), nextResults)

      case 0 => replicationResults
    }

    loop(replications, initialPopulation, initialIndividual, replicationStarters)
  }

  case class ReplicationResults(totalBestEntropy: Double,
                                totalBestGeneration: Double,
                                allTimeBestEntropy: Double,
                                allTimeWorstEntropy: Double)

  /**
    * Read a traffic file to parse the content as a traffic matrix.
    * @param file is the file containing the information
    * @return a traffic matrix
    */
  private def readTraffic(file: File): Traffic = {
    val bufferedSource = Source.fromFile(file)
    val traffic = for (
      line <- bufferedSource.getLines
    ) yield {
      line.split(" ").map(_.toDouble)
    }
//    bufferedSource.close

    Traffic(traffic.filter(_.nonEmpty).toArray)
  }
}
