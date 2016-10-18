package com.kduggan.spark.worldcup

import com.kduggan.spark.worldcup.model.WorldCup.MatchEvent
import org.apache.spark.rdd.RDD

import scala.collection.Map

/**
 * build an inverted index that you could use for fast
 * lookups
 */
object Ex4EventsByCountry {

  /**
   * you could use groupByKey..but..there is a more performant way!
   */
  def getSearchIndex(matchEvents:RDD[MatchEvent]): Map[String, List[MatchEvent]] ={
    val keyValues = matchEvents.map(matchEvent => (matchEvent.team, matchEvent))
    val append = (s: List[MatchEvent], v: MatchEvent) => v :: s
    val mergePartitionSets = (p1: List[MatchEvent], p2: List[MatchEvent]) => p1 ++ p2
    val invertedIndex:RDD[(String, List[MatchEvent])] =  keyValues.aggregateByKey(List[MatchEvent]())(append, mergePartitionSets)
    invertedIndex.collectAsMap()
  }

}
