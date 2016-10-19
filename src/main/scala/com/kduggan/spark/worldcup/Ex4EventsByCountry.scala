package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup.MatchEvent
import org.apache.spark.rdd.RDD

import scala.collection.Map

/**
 * build an inverted index that you could use for fast
 * lookups for match events by country
 */
object Ex4EventsByCountry {

  /**
   * you could use groupByKey..but..there is a more performant way!
   */
  def getSearchIndex(matchEvents:RDD[MatchEvent]): Map[String, List[MatchEvent]] ={
   ???
  }

}
