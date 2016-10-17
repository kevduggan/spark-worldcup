package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup.MatchEvent
import org.apache.spark.{SparkContext, SparkConf}
import org.scalatest.{BeforeAndAfterAll, Matchers, FunSuite}

import scala.collection.Map

class Ex3TopScorersSpec extends FunSuite with Matchers with BeforeAndAfterAll{
  val sparkConf = new SparkConf()
    .setAppName("SparkWorldCupSpec-Ex3EventsByCountry")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[2]")

  val sc = new SparkContext(sparkConf)

  override def afterAll() {
    sc.stop()
  }

  test("events search"){
    val matchEventData = DataLoader.loadEventData(sc)
    val scorers:Array[(String, Int)] = Ex3TopScorers.topFiveScorers(matchEventData)
    scorers.length should be (5)
    scorers(0)._1 should be ("james")
    scorers(0)._2 should be (6)
    scorers(1)._1 should be ("müller")
    scorers(1)._2 should be (5)
  }
}