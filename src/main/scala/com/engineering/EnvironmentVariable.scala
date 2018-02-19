package com.engineering

import enumeratum.values.{StringEnum, StringEnumEntry}

import scala.collection.immutable.IndexedSeq

object EnvironmentVariable {

  sealed abstract class EnvironmentVariable(val value: String) extends StringEnumEntry

  object EnvironmentVariable extends StringEnum[EnvironmentVariable] {

    override def values: IndexedSeq[EnvironmentVariable] = findValues

    case object APPLICATION extends EnvironmentVariable("APPLICATION")

    case object OUTPUT_FOLDER_PATH extends EnvironmentVariable("OUTPUT_FOLDER_PATH")

  }
}
