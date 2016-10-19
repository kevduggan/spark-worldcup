package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.data.DataLoader
import com.kduggan.spark.worldcup.model.WorldCup.MatchEvent
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}

class Ex3TopScorersSpec extends FunSuite with Matchers with BeforeAndAfterAll{
  val sparkConf = new SparkConf()
    .setAppName("Ex3TopScorersSpec")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[2]")

  val sc = new SparkContext(sparkConf)

  override def afterAll() {
    sc.stop()
  }

  test("should get the top scorers"){
    val matchEventData: RDD[MatchEvent] = DataLoader.loadEventData(sc)
    val scorers:Array[(String, Int)] = Ex3TopScorers.topFiveScorers(matchEventData)
    scorers.length should be (5)
    scorers(0)._1 should be ("james")
    scorers(0)._2 should be (6)
    scorers(1)._1 should be ("müller")
    scorers(1)._2 should be (5)
  }
}
