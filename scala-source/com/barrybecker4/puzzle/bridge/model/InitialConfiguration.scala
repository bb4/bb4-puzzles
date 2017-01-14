// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model


case class InitialConfiguration(label: String, peopleSpeeds: Array[Int], shortestPath: Int) {
  def getLabel: String = label + ": " + peopleSpeeds.mkString(", ")
}

object InitialConfiguration {

  val CONFIGURATIONS = Seq(
    InitialConfiguration("Standard Problem", Array(1, 2, 5, 8), 15),
    InitialConfiguration("Alternative Problem", Array(5, 10, 20, 25), 60),
    InitialConfiguration("Hard Problem", Array(1, 2, 5, 7, 8, 12, 15), 47),
    InitialConfiguration("Harder Problem", Array(7, 11, 2, 3, 5, 4, 1, 3, 12, 3, 15, 19, 8), 74),
    InitialConfiguration("Big Problem", Array(7, 11, 2, 3, 17, 5, 4, 3, 12, 7, 14, 16, 1, 3, 12, 3, 15, 19, 8), 126),
    InitialConfiguration("Trivial Problem", Array(1, 2, 5), 8)
  )
}
