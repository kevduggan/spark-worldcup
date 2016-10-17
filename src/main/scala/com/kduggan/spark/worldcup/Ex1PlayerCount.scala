package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup.Player
import org.apache.spark.rdd.RDD

object Ex1PlayerCount {

  /**
   * How many players names begin
   */
  def getPlayersBeginningWith(letter:String, players:RDD[Player]): Int ={
    players.filter(player =>  player.name.startsWith(letter)).count().toInt
  }



}
