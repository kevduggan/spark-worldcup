package com.kduggan.spark.worldcup

import org.apache.spark.{SparkContext, SparkConf}
import org.scalatest._

class Ex2TopClubSpec extends FunSuite with Matchers with BeforeAndAfterAll{

  val sparkConf = new SparkConf()
    .setAppName("SparkWorldCupSpec-Ex2TopClubSpec")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[2]")

  val sc = new SparkContext(sparkConf)

  override def afterAll() {
    sc.stop()
  }

  test("countWord should count the occurrences of each word"){
    val playerData = DataLoader.loadPlayerData(sc)
    val club = Ex2TopClub.getTopClub(playerData)
    club should be ("Barcelona")
  }

}

