package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.data.DataLoader
import com.kduggan.spark.worldcup.model.WorldCup.Player
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest._

class Ex1PlayerCountSpec extends FunSuite with Matchers with BeforeAndAfterAll{

  val sparkConf = new SparkConf()
    .setAppName("Ex1PlayerCountSpec")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[2]")

  val sc = new SparkContext(sparkConf)

  override def afterAll() {
    sc.stop()
  }

  test("count the players with names beginning with H"){
    val playerData:RDD[Player] = DataLoader.loadPlayerData(sc)
    val playerCount = Ex1PlayerCount.getPlayersBeginningWith("H", playerData)
    playerCount should be (24)
  }

}
