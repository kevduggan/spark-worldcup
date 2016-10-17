import AssemblyKeys._
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
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
  "com.github.melrief" %% "purecsv" % "0.0.6",
  compilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

)

//sbt-assembly
assemblySettings

jarName in assembly := name.value + ".jar"

mainClass in assembly := Some("com.kduggan.spark.worldcup.SparkWorldCup")

test in assembly := {}

logBuffered := false

parallelExecution in Test := false

parallelExecution in ThisBuild := false

val meta = """META.INF(.)*""".r

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case PathList(prop @ _*) if prop.last endsWith ".properties" => MergeStrategy.first
    case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.first
    case PathList("com", "codahale", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache", xs@_*) => MergeStrategy.first
    case PathList("javax.transaction", "transaction", xs@_*) => MergeStrategy.first
    case PathList("javax", "activation", xs@_*) => MergeStrategy.first
    case PathList("com", "google", "common", "base", xs@_*) => MergeStrategy.first
    case meta(_) => MergeStrategy.discard
    case x => old(x)
  }
}

// Statement coverage

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 80

ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := true

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := false

// Code style

org.scalastyle.sbt.ScalastylePlugin.Settings

org.scalastyle.sbt.PluginKeys.failOnError := true

org.scalastyle.sbt.PluginKeys.config <<= baseDirectory { _ / "src/main/config" / "scalastyle-config.xml" }

wartremoverErrors ++= Warts.allBut(Wart.NoNeedForMonad, Wart.AsInstanceOf, Wart.IsInstanceOf)
