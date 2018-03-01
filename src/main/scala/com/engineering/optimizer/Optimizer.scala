package com.engineering.optimizer

import java.io.File
import java.nio.file.Paths

import com.engineering.EnvironmentVariable.EnvironmentVariable.TRAFFIC_FOLDER_PATH
import com.engineering.model.{Individual, Population}
import com.engineering.utils.FileTools

import scala.io.Source
import scala.util.Properties

object Optimizer extends App {
  override def main(args: Array[String]): Unit = {
    println("OPTIMIZATION STARTED")

    Properties.envOrNone(s"$TRAFFIC_FOLDER_PATH") match {
      case Some(trafficFolder) =>
        FileTools.getFilesList(Paths.get(trafficFolder)) match {
          case Right(files) => files.foreach(file => {
            val replications = 100

            val (totalBestEntropy, totalBestGeneration, allTimeBestEntropy, allTimeWorstEntropy) = testTraffic(file, replications)

            println("\nAverage entropy of the strongest: " + totalBestEntropy / replications)
            println("\nAverage generation of the strongest: " + totalBestGeneration / replications)
            println("\nBest entropy of all time: " + allTimeBestEntropy)
            println("\nWorst entropy of all time: "+ allTimeWorstEntropy)
          })
          case Left(error) => println(error)
        }

      case None => println("TRAFFIC_FOLDER_PATH is not defined")

    }
  }

  private def testTraffic(file: File, replications: Int): (Double, Double, Double, Double) = {
    val resultsFileName: String = "RES_" + file.toString

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

  private def readTraffic(file: File): Array[Array[Double]] = {
    val bufferedSource = Source.fromFile(file)
    val traffic = for (
      line <- bufferedSource.getLines
    ) yield {
      line.split(" ").map(_.toDouble)
    }
    bufferedSource.close

    traffic.filter(_.nonEmpty).toArray
  }
}
