package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup
import com.kduggan.spark.worldcup.model.WorldCup.Player
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}

object DataLoader {

  def loadPlayerData(sc: SparkContext): RDD[Player] = {
    val sqlContext = new SQLContext(sc)
    val playerDataFrame:DataFrame = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("players.csv")
    playerDataFrame.map(x=>Player(x.getAs[Int]("jersey"), x.getAs[String]("position"), x.getAs[Int]("age"), x.getAs[Int]("selections"), x.getAs[String]("club"), x.getAs[String]("name"), x.getAs[String]("country")))
  }

  def loadCountryData(sc: SparkContext): RDD[WorldCup.Country] = {
    val sqlContext = new SQLContext(sc)
    val countryDataFrame:DataFrame = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("countries.csv")
    countryDataFrame.map(x=>WorldCup.Country(x.getAs[String]("group"), x.getAs[String]("country"), x.getAs[String]("code"),x.getAs[Int]("rank")))
  }

  def loadEventData(sc: SparkContext): RDD[WorldCup.MatchEvent] = {
    val sqlContext = new SQLContext(sc)
    val eventDataFrame:DataFrame = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .load("events.csv")
    eventDataFrame.map(x=>WorldCup.MatchEvent(x.getAs[Int]("id"), x.getAs[String]("player"), x.getAs[String]("team"), x.getAs[String]("type"), x.getAs[Long]("created_at"), x.getAs[Int]("match_id")))
  }
}
