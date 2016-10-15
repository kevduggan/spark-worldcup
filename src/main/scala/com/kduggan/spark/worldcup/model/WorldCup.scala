package com.kduggan.spark.worldcup.model

object WorldCup {

  case class Country(val group: String, val country: Int, val rank: Int)
  case class Player (val jersey:Int, val position:String, val age:Int, val selections:Int, val club:String, val name:String, val captain:Int)

}
