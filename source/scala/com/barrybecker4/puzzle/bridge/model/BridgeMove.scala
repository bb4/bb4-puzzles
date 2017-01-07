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
                       var direction: Boolean)

/**
  * create a move object representing a transition on the board.
  */
  extends Move with Comparable[BridgeMove1] {
  cost = determineCost
  /** The time for the slowest person out of everyone crossing at the same time */
  private var cost = 0

  //def getPeople: List[Int] = people
  //def getDirection: Boolean = direction

  private def determineCost = {
    if (people.size == 1) people.head else Math.max(people.head, people(1))
  }

  def getCost: Int = cost

  /** @return the from and to positions */
  override def toString: String =
    "people: " + people + (if (direction) " -> " else " <- ")

  override def equals(o: Any): Boolean = {
    if (this.equals(o)) return true
    if (o == null || (getClass ne o.getClass)) return false
    val that = o.asInstanceOf[BridgeMove]
    direction == that.direction && people == that.people
  }

  override def hashCode: Int = {
    var result = if (direction) 1
    else 0
    result = 31 * result + people.hashCode
    result
  }

  def compareTo(m: BridgeMove1): Int = getCost - m.getCost
}

