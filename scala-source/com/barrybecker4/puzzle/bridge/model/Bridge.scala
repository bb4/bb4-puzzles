// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model


/**
  * Immutable representation a bridge and the state at which people have crossed or not.
  * @param uncrossed the speeds of the people that have not crossed yet
  * @param crossed the the people (really their speeds) that have crossed
  * @author Barry Becker
  */
case class Bridge(uncrossed: List[Int], crossed: List[Int], lightCrossed: Boolean) {

  /**
    * Constructor that creates an initial bridge state
    * @param people array of people (represented by their speeds) that need to cross the bridge.
    */
  def this(people: Array[Int]) {
    this(people.toList, List[Int](), false)
  }

  /**
    * Create a new bridge state by applying th specified move
    * @param move    the move to apply
    * @param reverse if false then moving across the bridge, if true then revers traversal back to the start.
    */
  def applyMove(move: BridgeMove, reverse: Boolean): Bridge = {
    var uncrossedPeople = List[Int]()
    var crossedPeople = List[Int]()
    uncrossedPeople ++= uncrossed
    crossedPeople ++= crossed
    val direction = reverse != move.direction
    if (direction) {
      crossedPeople ++= move.people
      uncrossedPeople = uncrossedPeople.filter(x => !move.people.contains(x))
    }
    else {
      crossedPeople = crossed.filter(x => !move.people.contains(x))
      uncrossedPeople ++= move.people
    }
    Bridge(uncrossedPeople, crossedPeople, direction)
  }

  /**
    * @return true when everyone has crossed the bridge.
    */
  def isSolved: Boolean = uncrossed.isEmpty

  /** @return the sum of the speeds of the people to cross is a rough estimate of the distance to the goal */
  def distanceFromGoal: Int = {
    var sum = 0
    for (person <- uncrossed) {
      sum += person
    }
    sum
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Bridge]

  /*
  override def equals(other: Any): Boolean = other match {
    case that: Bridge =>
      (that canEqual this) &&
        uncrossed == that.uncrossed &&
        crossed == that.crossed &&
        lightCrossed == that.lightCrossed
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(uncrossed, crossed, lightCrossed)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }*/
}
