package com.engineering.pathmaker

import com.engineering.EnvironmentVariables.EnvironmentVariable.TRAFFIC_FOLDER_PATH
import com.engineering.model.Traffic
import com.engineering.pathmaker.Context._
import com.engineering.utils.FileTools


/**
  * PathMaker application.
  */
object PathMaker extends App {

  /**
    * Main method.
    */
  override def main(args: Array[String]): Unit = {

    println("PathMaker just started...")

    val variations = Set(Canonical, Disrupted)
    variations.foreach(generateTrafficFiles(_))

    println("\nFinished")
  }

  /**
    * Build and save traffic matrices that will be used in the Optimizer.
    * @param context of production environment
    */
  def generateTrafficFiles(context: Variation): Unit = {
    val fileStem = context match {
      case Canonical => "CANONICAL_" + nbMachines + "_"
      case Disrupted => "DISRUPTED_" + nbMachines + "_"
    }

    val productMix = buildProductMix(nbEnvironments, nbProducts, context.mask)

    // FileWriter
    FileTools.getFolderPath(TRAFFIC_FOLDER_PATH) match {
      case Right(path) =>
        Range(0, nbEnvironments).foreach(environment => {
          val traffic = Traffic(nbMachines, environment, ProductLine.routing, productMix)
          FileTools.saveFileToFolder(path, fileStem + environment, traffic.toString)
        })

      case Left(error) => println(error)
    }
  }

  /**
    * Build a productMix matrix.
    * @param nbEnvironments number of environments
    * @param nbProducts number of products
    * @param mask modulation of product mix per environment
    * @return product mix per environment
    */
  def buildProductMix(nbEnvironments: Int,
                      nbProducts: Int,
                      mask: Array[Array[Int]]): Array[Array[Double]] = {

    val initial = Array(100, 100, 100)
    mask.map(products => {
      val tmp = initial.zip(products).map {
        case (x, y) => x * (1 + y/100.0)
      }
      tmp.map(z => math.floor(100 * (100 * z/tmp.sum)) / 100)
    })
  }
}
