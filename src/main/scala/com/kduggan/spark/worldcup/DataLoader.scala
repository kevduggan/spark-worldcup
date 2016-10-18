package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup
import com.kduggan.spark.worldcup.model.WorldCup.Player
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}

/**
 * Functions to load in the raw data.
 * Uses the spark_csv library that is built on top of spark_sql
 *
 * You do not need to touch this
 */
object DataLoader {

  def loadPlayerData(sc: SparkContext): RDD[Player] = {
    loadCSV(sc, "players.csv").map(player => Player(player.getAs[Int]("jersey"), player.getAs[String]("position"), player.getAs[Int]("age"), player.getAs[Int]("selections"), player.getAs[String]("club"), player.getAs[String]("name"), player.getAs[String]("country")))
  }

  def loadCountryData(sc: SparkContext): RDD[WorldCup.Country] = {
      loadCSV(sc, "countries.csv").map(country => WorldCup.Country(country.getAs[String]("group"), country.getAs[String]("country"), country.getAs[String]("code"),country.getAs[Int]("rank")))
  }

  def loadEventData(sc: SparkContext): RDD[WorldCup.MatchEvent] = {
    loadCSV(sc, "events.csv").map(event => WorldCup.MatchEvent(event.getAs[Int]("id"), event.getAs[String]("player"), event.getAs[String]("team"), event.getAs[String]("type"), event.getAs[Long]("created_at"), event.getAs[Int]("match_id")))
  }

  def loadCSV(sc: SparkContext, filename: String): DataFrame = {
    val sqlContext = new SQLContext(sc)
    val playerDataFrame: DataFrame = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(filename)
    playerDataFrame
  }
}
