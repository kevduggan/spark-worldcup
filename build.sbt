import scoverage.ScoverageSbtPlugin

name := "spark-worldcup"

organization := "com.kduggan.spark.worldcup"

version := "1.0"

//spark only compatible with scala 2.10.x
scalaVersion := "2.10.4"

//Define dependencies.
libraryDependencies ++= Seq(
  // logging
  "com.typesafe" % "scalalogging-slf4j_2.10" % "1.1.0",
  // guava
  "com.google.guava" % "guava" % "16.0",
  //spark
  "org.apache.spark" %% "spark-core" % "1.4.1" % "provided"
    exclude("org.slf4j", "slf4j-api")
    exclude("com.google.guava", "guava"),
  "org.apache.spark" %% "spark-sql" % "1.4.1",
  "com.databricks" % "spark-csv_2.10" % "1.5.0",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
)


logBuffered := false

parallelExecution in Test := false

parallelExecution in ThisBuild := false

// Statement coverage

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 80

ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := true

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := false

// Code style

org.scalastyle.sbt.ScalastylePlugin.Settings

org.scalastyle.sbt.PluginKeys.failOnError := true

org.scalastyle.sbt.PluginKeys.config <<= baseDirectory { _ / "src/main/config" / "scalastyle-config.xml" }

wartremoverErrors ++= Warts.allBut(Wart.NoNeedForMonad, Wart.AsInstanceOf, Wart.IsInstanceOf)
