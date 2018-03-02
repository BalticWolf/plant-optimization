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
    "org.specs2" %% "specs2-core" % "4.0.2" % Test
  ))
