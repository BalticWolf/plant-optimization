package com.engineering

import enumeratum.values.{StringEnum, StringEnumEntry}

object EnvironmentVariable {

  sealed abstract class EnvironmentVariable(val value: String) extends StringEnumEntry

  case object EnvironmentVariable extends StringEnum[EnvironmentVariable] {

    override def values: IndexedSeq[EnvironmentVariable] = findValues

    case object APPLICATION extends EnvironmentVariable("APPLICATION")

  }
}
