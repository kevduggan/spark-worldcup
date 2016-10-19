package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.data.DataLoader
import com.kduggan.spark.worldcup.model.WorldCup.MatchEvent
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest._

import scala.collection.Map

class Ex4EventsByCountrySpec extends FunSuite with Matchers with BeforeAndAfterAll{

  val sparkConf = new SparkConf()
    .setAppName("Ex4EventsByCountrySpec")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[2]")

  val sc = new SparkContext(sparkConf)

  override def afterAll() {
    sc.stop()
  }

  test("events search"){
    val matchEventData = DataLoader.loadEventData(sc)
    val index:Map[String, List[MatchEvent]] = Ex4EventsByCountry.getSearchIndex(matchEventData)
    index.get("GER") match {
      case Some(events) => events.count(_.eventType.contains("goal")) shouldBe 18
      case None => assert(false)
    }
    index.get("BRA") match {
      case Some(events) => events.count(_.eventType.contains("card")) shouldBe 14
      case None => assert(false)
    }
  }

}


