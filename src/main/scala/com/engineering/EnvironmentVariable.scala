package com.engineering

import enumeratum.values.{StringEnum, StringEnumEntry}

import scala.collection.immutable.IndexedSeq

object EnvironmentVariable {

  sealed abstract class EnvironmentVariable(val value: String) extends StringEnumEntry

  object EnvironmentVariable extends StringEnum[EnvironmentVariable] {

    override def values: IndexedSeq[EnvironmentVariable] = findValues

    case object APPLICATION extends EnvironmentVariable("APPLICATION")

    case object TRAFFIC_FOLDER_PATH extends EnvironmentVariable("TRAFFIC_FOLDER_PATH")

    case object INDIVIDUALS_FOLDER_PATH extends EnvironmentVariable("INDIVIDUALS_FOLDER_PATH")

    case object RESULTS_FOLDER_PATH extends EnvironmentVariable("RESULTS_FOLDER_PATH")

  }
}
