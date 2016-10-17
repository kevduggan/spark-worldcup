package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup
import com.typesafe.scalalogging.slf4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s._
import org.slf4j.LoggerFactory
import purecsv.unsafe._


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
    val countries:RDD[WorldCup.Country] = loadCountryData(sc)
    //val players:RDD[WorldCup.Player] = loadPlayerData(sc)

    //val players = loadPlayerDataWithCountry(sc).collect.toSeq.writeCSVToFileName("players_1.csv");
//    val countries = loadCountryData(sc).collect.toSeq.writeCSVToFileName("countries.csv", header=Some(Seq("group","country","rank")));
//      val players:Seq[WorldCup.Player] = loadPlayerData(sc).collect.toSeq
//      players.writeCSVToFileName("players.csv")
  }

  def loadCountryData(sc: SparkContext): RDD[WorldCup.Country] = {
    val sqlContext = new SQLContext(sc)
    val countryDataFrame:DataFrame = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("countries.csv")
    countryDataFrame.map(x=>WorldCup.Country(x.getAs[String]("group"), x.getAs[String]("country"), x.getAs[String]("code"),x.getAs[Int]("rank"))).distinct()
  }

  def loadPlayerData(sc: SparkContext): RDD[WorldCup.Player] = {
    val sqlContext = new SQLContext(sc)
    val playerDataFrame:DataFrame = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("players.csv")
    playerDataFrame.map(x=>WorldCup.Player(x.getAs[Int]("jersey"), x.getAs[String]("position"), x.getAs[Int]("age"), x.getAs[Int]("selections"), x.getAs[String]("club"), x.getAs[String]("name"), x.getAs[String]("country")))
  }


}