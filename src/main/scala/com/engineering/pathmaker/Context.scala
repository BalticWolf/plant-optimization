package com.engineering.pathmaker


/**
  * Define elements of production context
  */
object Context {
  lazy val nbEnvironments = 7
  lazy val nbProducts = 3
  lazy val nbMachines = 25

  /**
    * Define standard variations in production.
    */
  sealed trait Variation {
    def mask: Array[Array[Int]] = Array.ofDim[Int](nbEnvironments, nbProducts)
  }

  /**
    * Define a canonical context.
    */
  case object Canonical extends Variation {

    private lazy val delta = 100

    override val mask = Array(
      Array(0, 0, 0),
      Array(0, 0, delta),
      Array(0, delta, 0),
      Array(0, delta, delta),
      Array(delta, 0, 0),
      Array(delta, 0, delta),
      Array(delta, delta, 0)
    )
  }

  /**
    * Define a disrupted context.
    */
  case object Disrupted extends Variation {
    override val mask = Array(
      Array(70, 30, 70),
      Array(70, 70, 0),
      Array(70, 70, 30),
      Array(70, 0, 30),
      Array(70, 0, 70),
      Array(70, 30, 0),
      Array(70, 30, 30)
    )
  }

  /**
    * Define a template for product routing.
    */
  sealed trait ManufacturingPlan {

    /**
      * Represent the routing of products.
      * A routing is a sequence of machine numbers, each representing a manufacturing step.
      */
    def routing: Array[Array[Int]] = Array.ofDim[Int](nbProducts, nbMachines)
  }

  case object ProductLine extends ManufacturingPlan {
    override val routing = Array(
      Array(0,1,2,3,0,6,7,8,9,6,13,14,15,16,13,18,19,20,21,18),
      Array(3,2,15,17,3,22,23,4,5,22,9,7,11,19,9,8,1,6,14,8),
      Array(24,23,22,21,24,18,17,16,15,18,12,11,10,9,12,7,2,3,4,7)
    )
  }
}
