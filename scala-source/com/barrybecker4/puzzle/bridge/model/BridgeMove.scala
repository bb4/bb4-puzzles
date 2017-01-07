// Copyright by Barry G. Becker, 20017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import com.barrybecker4.puzzle.common.model.Move


/**
  * Represents the act of one or two people crossing the bride.
  * At most two people can cross at once or the bridge may crash.
  * The light/torch is always transferred with the people that are crossing.
  * Immutable.
  * TODO: make case class and remove equals/hashcode?
  * @param people the speeds of the person or people that are crossing
  * @param direction if true then crossing
  * @author Barry Becker
  */
final class BridgeMove(var people: List[Int],
                       var direction: Boolean) extends Move with Comparable[BridgeMove] {
  /** The time for the slowest person out of everyone crossing at the same time */
  private val cost = determineCost

  //def getPeople: List[Int] = people
  //def getDirection: Boolean = direction

  private def determineCost = {
    if (people.size == 1) people.head else Math.max(people.head, people(1))
  }

  def getCost: Int = cost

  /** @return the from and to positions */
  override def toString: String =
    "people: " + people + (if (direction) " -> " else " <- ")


  def compareTo(m: BridgeMove): Int = getCost - m.getCost

  override def equals(other: Any): Boolean = other match {
    case that: BridgeMove =>
      direction == that.direction && people == that.people
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(cost)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

