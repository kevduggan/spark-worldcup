package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.data.DataLoader
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest._

class Ex2TopClubSpec extends FunSuite with Matchers with BeforeAndAfterAll{

  val sparkConf = new SparkConf()
    .setAppName("Ex2TopClubSpec")
    .set("spark.driver.allowMultipleContexts", "true")
    .setMaster("local[2]")

  val sc = new SparkContext(sparkConf)

  override def afterAll() {
    sc.stop()
  }

  test("should calculate the more represented club"){
    val playerData = DataLoader.loadPlayerData(sc)
    val club = Ex2TopClub.getTopClub(playerData)
    club should be ("Barcelona")
  }

}

