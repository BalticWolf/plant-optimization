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

                  val (totalBestEntropy, totalBestGeneration, allTimeBestEntropy, allTimeWorstEntropy) = testTraffic(file, rep)

                  println("\nAverage entropy of the strongest: " + totalBestEntropy / rep)
                  println("\nAverage generation of the strongest: " + totalBestGeneration / rep)
                  println("\nBest entropy of all time: " + allTimeBestEntropy)
                  println("\nWorst entropy of all time: "+ allTimeWorstEntropy)

                case Failure(error) => println(error)
              }
            })
          case Left(error) => println(error)
        }
      case Left(error) => println(error)
    }
    println("\nFinished")
  }

  private def testTraffic(file: File, replications: Int): (Double, Double, Double, Double) = {
    val resultsFileName: String = "RES_" + file.getName

    val strongest = Individual() // creation of the primordial individual

    val traffic = readTraffic(file)

    def loop(replications: Int,
             strongest: Individual,
             totalBestEntropy: Double,
             totalBestGeneration: Double,
             allTimeBestEntropy: Double,
             allTimeWorstEntropy: Double
            ): (Double, Double, Double, Double) = replications match {
      case r if r > 0 =>

        val population = Population()

        val bestEntropy = strongest.entropy

        population.lifeCycle(traffic, strongest)

        val reAssessedEntropy = strongest.evaluate(traffic).entropy
        strongest.writeToFile(resultsFileName)

        loop(replications - 1,
          strongest.reset(),
          totalBestEntropy + reAssessedEntropy,
          totalBestGeneration + population.bestGeneration,
          math.min(bestEntropy, allTimeBestEntropy),
          math.max(bestEntropy, allTimeWorstEntropy)
        )
      case 0 => (totalBestEntropy, totalBestGeneration, allTimeBestEntropy, allTimeWorstEntropy)
    }

    loop(replications, strongest, 0, 0, Individual.defaultEntropy, Individual.minimumEntropy)
  }

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
