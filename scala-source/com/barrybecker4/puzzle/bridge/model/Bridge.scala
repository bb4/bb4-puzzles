// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model


/**
  * Immutable representation a bridge and the state at which people have crossed or not.
  *
  * @author Barry Becker
  */
class Bridge(val uncrossed: List[Int], val crossed: List[Int], var lightCrossed: Boolean) {

  /** Represents the speeds of the people that have not crossed yet */
  private val peopleUncrossed = uncrossed

  /** Represents the the people (really their speeds) that have crossed */
  private val peopleCrossed = crossed


  /**
    * Constructor that creates an initial bridge state
    *
    * @param people array of people (represented by their speeds) that need to cross the bridge.
    */
  def this(people: Array[Int]) {
    this(people.toList, List[Int](), false)
  }

  /**
    * Constructor
    * create a new bridge state by applying th specified move
    *
    * @param move    the move to apply
    * @param reverse if false then moving across the bridge, if true then revers traversal back to the start.
    */
  def applyMove(move: BridgeMove, reverse: Boolean): Bridge = {
    var uncrossed = List[Int]()
    var crossed = List[Int]()
    uncrossed ++= getUncrossedPeople
    crossed ++= getCrossedPeople
    val direction = reverse != move.direction
    if (direction) {
      crossed ++= move.people
      uncrossed = uncrossed.filter(x => !move.people.contains(x))
    }
    else {
      crossed = crossed.filter(x => !move.people.contains(x))
      uncrossed ++= move.people
    }
    new Bridge(uncrossed, crossed, direction)
  }

  def getUncrossedPeople: List[Int] = peopleUncrossed

  def getCrossedPeople: List[Int] = peopleCrossed

  def isLightCrossed: Boolean = lightCrossed

  /**
    * @return true when everyone has crossed the bridge.
    */
  def isSolved: Boolean = peopleUncrossed.isEmpty

  /** @return the sum of the speeds of the people to cross is a rough estimate of the distance to the goal */
  def distanceFromGoal: Int = {
    var sum = 0
    for (person <- peopleUncrossed) {
      sum += person
    }
    sum
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Bridge]

  override def equals(other: Any): Boolean = other match {
    case that: Bridge =>
      (that canEqual this) &&
        peopleUncrossed == that.peopleUncrossed &&
        peopleCrossed == that.peopleCrossed &&
        lightCrossed == that.lightCrossed
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(peopleUncrossed, peopleCrossed, lightCrossed)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  /*
  override def equals(o: Any): Boolean = {
    if (this == o) return true
    if (o == null || (getClass ne o.getClass)) return false
    val bridge = o.asInstanceOf[Bridge]

    lightCrossed == bridge.lightCrossed && peopleCrossed == bridge.peopleCrossed &&
      peopleUncrossed == bridge.peopleUncrossed
  }

  override def hashCode: Int = {
    var result = peopleUncrossed.hashCode
    result = 31 * result + peopleCrossed.hashCode
    result = 31 * result + (if (lightCrossed) 1
    else 0)
    result
  }*/
}
