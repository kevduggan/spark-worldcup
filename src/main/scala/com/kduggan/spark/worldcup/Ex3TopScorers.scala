package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup.MatchEvent
import org.apache.spark.rdd.RDD

/**
 * More fun with key-value pairs
 */
object Ex3TopScorers {

  /**
   * Top 5 scorers with number of goals scored by each
   * 
   * Tip: there are inconsistencies in the source data with capitalisation
   * Tip: Penalties are goals too!!!
   */
  def topFiveScorers(matchEvents:RDD[MatchEvent]): Array[(String, Int)] ={
    ???
  }
}
