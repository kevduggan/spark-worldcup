package com.kduggan.spark.worldcup.model

object WorldCup {

  case class Country(val group: String, val country: String, val code: String, val rank: Int)
  case class Player(val jersey:Int, val position:String, val age:Int, val selections:Int, val club:String, val name:String, val country:String)
  case class MatchEvent(val id:Int, val player:String, val team:String, val eventType:String, val createdAt:Long, val match_id:Int)
}
