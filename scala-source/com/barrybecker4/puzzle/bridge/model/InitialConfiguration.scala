// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model


case class InitialConfiguration(label: String, peopleSpeeds: Array[Int]) {
  def getLabel: String = label + ": " + peopleSpeeds.mkString(", ")
}

object InitialConfiguration {

  val STANDARD_PROBLEM = InitialConfiguration("Standard Problem", Array(1, 2, 5, 8)) // shortest = 15
  val ALTERNATIVE_PROBLEM = InitialConfiguration("Alternative Problem", Array(5, 10, 20, 25))
  val DIFFICULT_PROBLEM = InitialConfiguration("Hard Problem", Array(1, 2, 5, 7, 8, 12, 15))
  val HARDER_PROBLEM = InitialConfiguration("Harder Problem", Array(7, 11, 2, 3, 5, 4, 1, 3, 12, 3, 15, 19, 8))
  val BIG_PROBLEM = InitialConfiguration("Big Problem",
    Array(7, 11, 2, 3, 17, 5, 4, 3, 12, 7, 14, 16, 1, 3, 12, 3, 15, 19, 8))
  val TRIVIAL_PROBLEM = InitialConfiguration("Trivial Problem", Array(1, 2, 5))
}