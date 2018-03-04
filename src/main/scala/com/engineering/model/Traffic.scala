package com.engineering.model


case class Traffic(matrix: Array[Array[Double]]) {

  /**
    * Represent the amount of traffic between machines.
    */
  override def toString: String = {
    val result = for (
      i <- matrix.indices
    ) yield matrix(i).deep.mkString(" ")

    result.mkString("\n")
  }
}

object Traffic {

  /**
    * Build a traffic matrix.
    * @param environment index of environment
    * @param routes matrix of routing per product
    * @param productMix matrix of product mix
    */
  def apply(nbMachines: Int,
            environment: Int,
            routes: Array[Array[Int]],
            productMix: Array[Array[Double]]): Traffic = {

    val traffic = Array.ofDim[Double](nbMachines, nbMachines)
    for (product <- routes.indices) {
      for (step <- 0 until routes(product).length - 1) {
        traffic(routes(product)(step))(routes(product)(step + 1)) += productMix(environment)(product)
      }
    }
    Traffic(traffic)
  }
}
