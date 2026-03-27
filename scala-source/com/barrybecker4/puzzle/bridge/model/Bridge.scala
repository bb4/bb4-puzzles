// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model


/**
  * Immutable representation of a bridge and whether people have crossed or not.
  * Note: It would be more efficient to use Sets instead of Lists for crossed and uncrossed, but it currently runs fine.
  * @param uncrossed the speeds of the people that have not crossed yet
  * @param crossed the people (really their speeds) that have crossed
  * @author Barry Becker
  */
case class Bridge(uncrossed: List[Int], crossed: List[Int], lightCrossed: Boolean) {

  /**
    * Create a new bridge state by applying the specified move
    * @param move    the move to apply
    * @param reverse if false then moving across the bridge, if true then reverse traversal back to the start.
    */
  def applyMove(move: BridgeMove, reverse: Boolean): Bridge = {
    val moving = move.people.toSet
    val direction = reverse != move.direction
    if (direction) {
      Bridge(
        uncrossed.filterNot(moving.contains),
        crossed ++ move.people,
        direction
      )
    } else {
      Bridge(
        uncrossed ++ move.people,
        crossed.filterNot(moving.contains),
        direction
      )
    }
  }

  /** @return true when everyone has crossed the bridge. */
  def isSolved: Boolean = uncrossed.isEmpty

  /** @return the sum of the speeds of the people to cross is a rough estimate of the distance to the goal */
  def distanceFromGoal: Int = uncrossed.sum
}

object Bridge {

  /** Initial bridge state: everyone on the near side, torch not crossed. */
  def apply(people: Array[Int]): Bridge = Bridge(people.toList, Nil, lightCrossed = false)
}
