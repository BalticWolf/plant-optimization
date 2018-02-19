package com.engineering

import com.engineering.EnvironmentVariable.EnvironmentVariable.APPLICATION
import com.engineering.pathmaker.PathMaker
import com.engineering.optimizer.Optimizer

import scala.util.Properties

object MainApplication extends App {
  val app = Properties.envOrNone(s"$APPLICATION") match {
    case Some("PATH_MAKER") => Right(PathMaker)
    case Some("OPTIMIZER") => Right(Optimizer)
//    case Some("EVALUATE") => Right(new Evaluation)
    case Some(unknownApplication) => Left(s"APPLICATION '$unknownApplication' is not known")
    case None => Left(s"Environment variable '$APPLICATION' is not defined.")
  }

  app match {
    case Right(target) =>
      target.main(Array())
    case Left(reason) =>
      println(reason)
  }
}
