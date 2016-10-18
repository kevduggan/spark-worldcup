package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup.Player
import org.apache.spark.rdd.RDD

object Ex2TopClub {

  /**
   * http://spark.apache.org/docs/latest/programming-guide.html#working-with-key-value-pairs
   */
  def getTopClub(players:RDD[Player]): String ={
    val clubs:RDD[(String, Int)] = players.map(player => (player.club, 1))
    val clubsReduced:RDD[(String, Int)] = clubs.reduceByKey(_ + _)
    val ascending:Boolean = false
    clubsReduced.sortBy[Int](_._2, ascending).map(_._1).first()
  }

}
