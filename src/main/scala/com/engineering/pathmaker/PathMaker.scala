package com.engineering.pathmaker

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import com.engineering.EnvironmentVariable.EnvironmentVariable.TRAFFIC_FOLDER_PATH

import scala.io.StdIn
import scala.util.Properties

object PathMaker extends App {

  private lazy val delta = 100

  private lazy val canonicalMask = Array(
    Array(0, 0, 0),
    Array(0, 0, delta),
    Array(0, delta, 0),
    Array(0, delta, delta),
    Array(delta, 0, 0),
    Array(delta, 0, delta),
    Array(delta, delta, 0),
  )

  private lazy val disruptedMask = Array(
    Array(70, 30, 70),
    Array(70, 70, 0),
    Array(70, 70, 30),
    Array(70, 0, 30),
    Array(70, 0, 70),
    Array(70, 30, 0),
    Array(70, 30, 30),
  )

  private lazy val paths = Array(
    Array(0,1,2,3,0,6,7,8,9,6,13,14,15,16,13,18,19,20,21,18),
    Array(3,2,15,17,3,22,23,4,5,22,9,7,11,19,9,8,1,6,14,8),
    Array(24,23,22,21,24,18,17,16,15,18,12,11,10,9,12,7,2,3,4,7)
  )

  override def main(args: Array[String]): Unit = {
    var fileName: String = ""
    val nbEnv = 7
    val nbProducts = 3
    val nbMachines = 25
    var mask = Array.ofDim[Int](nbEnv, nbProducts)
    var ok = false

    println("PATH MAKING STARTED")

    do{
      println("\nCanonical (C) or Disrupted (D) ?")
      StdIn.readChar() match {
        case 'c' | 'C' => {
          mask = canonicalMask
          fileName = "CANON" + nbMachines + "_"
          ok = true
          println("\nCanonical environment selected")
        }
        case 'd' | 'D' => {
          mask = disruptedMask
          fileName = "DISRUPT" + nbMachines + "_"
          ok = true
          println("\nDisrupted environment selected")
        }
        case _ => println("Wrong choice. Try again.")
      }
    } while (!ok)

    println("\nPATHS")

    println("\nPRODUCT MIX")
    val mixProduct = Array.ofDim[Float](nbEnv, nbProducts)
    for(i <- 0 until nbProducts) {
      mixProduct(0)(i) = 100
    }
    for (k <- 0 until nbEnv) {
      for (j <- 0 until nbProducts) {
        mixProduct(k)(j) = mixProduct(0)(j) + mixProduct(0)(j) * mask(k)(j) / 100F
      }

      for (i <- 0 until nbProducts) {
        mixProduct(k)(i)=(mixProduct(k)(i)/mixProduct(k).sum)*100
      }

      println("\nEnvironment: " + k)
      for (i <- 0 until nbProducts) {
        println(" Prod." + i + ": " + mixProduct(k)(i))
      }
    }

    println("\nMAKE MATRICES")
    val nbPasses = 20
    val Traffic = Array.ofDim[Float](nbMachines, nbMachines)
    for (kk <- 0 until nbEnv) {
      for (i <- 0 until nbProducts) {
        for (j <- 0 until nbPasses) {
          Traffic(paths(i)(j))(paths(i)(j)) += mixProduct(kk)(i)
        }
      }

      println("\nMATRIX "+ kk)

      // FileWriter
      val completeFileName = fileName + kk
      val completePath = Properties.envOrNone(s"$TRAFFIC_FOLDER_PATH") match {
        case Some(path) => path + "/" + completeFileName
        case None => "src/main/output/traffic/" + completeFileName
      }

      val content = prepareOutput(nbMachines, Traffic)
      Files.write(Paths.get(completePath), content.getBytes(StandardCharsets.UTF_8))

    }
    println("\n\nFinished")
  }

  private def prepareOutput(dim: Int, arr: Array[Array[Float]]): String = {
    var result = dim + "\n"
    for (i <- 0 until dim) {
      for (j <- 0 until dim) {
        result += arr(i)(j) + " "
      }
      result += "\n"
    }
    result
  }
}
