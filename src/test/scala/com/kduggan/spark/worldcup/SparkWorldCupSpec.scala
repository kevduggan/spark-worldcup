package com.kduggan.spark.worldcup

import com.typesafe.scalalogging.slf4j.Logger
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest._
import org.slf4j.LoggerFactory

class SparkWorldCupSpec extends FlatSpec with Matchers with BeforeAndAfter with BeforeAndAfterAll {

  val logger = Logger(LoggerFactory.getLogger("SparkWorldCupSpec"))

  val sparkConf = new SparkConf()
    .setAppName("SparkWorldCupSpec")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[2]")

  val sparkContext = new SparkContext(sparkConf)

  override def afterAll() {
    sparkContext.stop()
  }

  "calculateTimeSeriesByChannel" should "process very simple data (1 entry) from raw table" in {
    SparkWorldCup.calculateStuff(sparkContext)

  }

}