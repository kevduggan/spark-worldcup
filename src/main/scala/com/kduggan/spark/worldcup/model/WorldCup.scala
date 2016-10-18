package com.kduggan.spark.worldcup.model

/**
 * case classes needed in the exercises
 */
object WorldCup {

  case class Country(group: String, country: String, code: String, rank: Int)
  case class Player(jersey:Int, position:String, age:Int, selections:Int, club:String, name:String, country:String)
  case class MatchEvent(id:Int, player:String, team:String, eventType:String, createdAt:Long, match_id:Int)
}
