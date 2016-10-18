package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup.MatchEvent
import org.apache.spark.rdd.RDD

object Ex3TopScorers {

  def topFiveScorers(matchEvents:RDD[MatchEvent]): Array[(String, Int)] ={
    val keyValues = matchEvents.filter(event => event.eventType.contains("goal")).map(matchEvent => (matchEvent.player.toLowerCase, 1))
    val scorers:RDD[(String, Int)] = keyValues.reduceByKey(_ + _)
    val ascending:Boolean = false
    scorers.sortBy[Int](_._2, ascending).take(5)
  }
}
