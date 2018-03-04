package com.engineering

import enumeratum.values.{StringEnum, StringEnumEntry}

import scala.collection.immutable.IndexedSeq
import scala.util.Properties

object EnvironmentVariables {

  sealed abstract class EnvironmentVariable(val value: String) extends StringEnumEntry

  object EnvironmentVariable extends StringEnum[EnvironmentVariable] {

    override def values: IndexedSeq[EnvironmentVariable] = findValues

    case object APPLICATION extends EnvironmentVariable("APPLICATION")

    case object REPLICATIONS extends EnvironmentVariable("REPLICATIONS")

    case object TRAFFIC_FOLDER_PATH extends EnvironmentVariable("TRAFFIC_FOLDER_PATH")

    case object INDIVIDUALS_FOLDER_PATH extends EnvironmentVariable("INDIVIDUALS_FOLDER_PATH")

    case object RESULTS_FOLDER_PATH extends EnvironmentVariable("RESULTS_FOLDER_PATH")

  }

  def envOrError(environmentVariable: EnvironmentVariable): Either[Error, String] = {
    Properties
      .envOrNone(s"$environmentVariable")
      .toRight(new Error(s"$environmentVariable environment variable is not defined"))
  }
}
