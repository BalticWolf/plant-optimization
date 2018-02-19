import sbt.Keys.libraryDependencies

lazy val commonSettings = Seq(
  name := "plant-optimization",
  version := "0.1",
  scalaVersion := "2.12.4"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "com.beachape" %% "enumeratum" % "1.5.12",
    "org.scalatest" %% "scalatest" % "3.0.4" % Test
  ))
