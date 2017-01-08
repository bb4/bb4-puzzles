// Copyright by Barry G. Becker, 20017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import com.barrybecker4.puzzle.common.model.Move

/**
  * Represents the act of one or two people crossing the bridge.
  * At most two people can cross at once or the bridge may crash.
  * The light/torch is always transferred with the people that are crossing.
  * @param people the speeds of the person or people that are crossing
  * @param direction if true then crossing
  */
case class BridgeMove(people: List[Int], direction: Boolean) extends Move with Comparable[BridgeMove] {

  /** The time for the slowest person out of everyone crossing at the same time */
  private val cost = if (people.size == 1) people.head else Math.max(people.head, people(1))

  /** @return the from and to positions */
  override def toString: String = "people: " + people + (if (direction) " -> " else " <- ")

  def compareTo(m: BridgeMove): Int = cost - m.cost
}

