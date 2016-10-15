package com.kduggan.spark.worldcup

import com.typesafe.scalalogging.slf4j.Logger
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s._
import org.slf4j.LoggerFactory


object SparkWorldCup {


  val logger = Logger(LoggerFactory.getLogger("ActivityBatchApp"))

  /**
    * needed by json library
    */
  implicit val formats = DefaultFormats

  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
      .setAppName("SparkWorldCup")
    val sc = new SparkContext(sparkConf)
    calculateStuff(sc)
    sc.stop()
  }

  /**
    * we split the logs into buckets, then calculate the daily activity metrics for each bucket
    * then the rollup happens whereby the newly generated daily metrics get added into the
    * larger granularity periods
    */
  def calculateStuff(sc: SparkContext): Unit = {
    val sqlContext = new SQLContext(sc)
    val playerData = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("player_info.csv")
  }

}