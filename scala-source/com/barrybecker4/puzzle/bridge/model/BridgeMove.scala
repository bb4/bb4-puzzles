// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import com.barrybecker4.puzzle.common.model.Move

/**
  * Represents the act of one or two people crossing the bridge.
  * At most two people can cross at once or the bridge may crash.
  * The light/torch is always transferred with the people that are crossing.
  * @param people the speeds of the person or people that are crossing
  * @param direction if true then crossing
  */
case class BridgeMove(people: List[Int], direction: Boolean) extends Move {

  require(people.nonEmpty && people.length <= 2, "A move must involve one or two people")

  /** The time for the slowest person out of everyone crossing at the same time */
  val cost: Int = people.max

  override def toString: String = "people: " + people + (if (direction) " -> " else " <- ")
}

object BridgeMove {

  given Ordering[BridgeMove] = Ordering.by(_.cost)
}
