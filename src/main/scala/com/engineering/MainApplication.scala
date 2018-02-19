package com.engineering

import com.engineering.EnvironmentVariable.EnvironmentVariable.APPLICATION

import scala.util.Properties

object MainApplication extends App {
  val app = Properties.envOrNone(s"$APPLICATION") match {
    case Some("MAKE_PATH") => Right(new PathMaker)
//    case Some("OPTIMIZE") => Right(new Optimizer)
//    case Some("EVALUATE") => Right(new Evaluation)
    case Some(unknownApplication) => Left(s"APPLICATION '$unknownApplication' is not known")
    case None => Left(s"Environment variable '$APPLICATION' is not defined.")
  }

  app match {
    case Right(target) =>
      target.main()
    case Left(reason) =>
      println(reason)
  }
}
