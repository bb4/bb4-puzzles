package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.puzzle.maze.model.Direction.{FORWARD, LEFT, RIGHT}

/**
  * Immutable counts of where FORWARD, LEFT, RIGHT appeared in each of the three shuffled positions.
  */
case class DistributionMap(
    forwardDist: List[Int] = List(0, 0, 0),
    leftDist: List[Int] = List(0, 0, 0),
    rightDist: List[Int] = List(0, 0, 0)
) {

  def increment(directions: List[Direction]): DistributionMap =
    incrAt(directions(0), 0).incrAt(directions(1), 1).incrAt(directions(2), 2)

  private def incrAt(dir: Direction, position: Int): DistributionMap = dir match {
    case FORWARD => copy(forwardDist = forwardDist.updated(position, forwardDist(position) + 1))
    case LEFT    => copy(leftDist = leftDist.updated(position, leftDist(position) + 1))
    case RIGHT   => copy(rightDist = rightDist.updated(position, rightDist(position) + 1))
  }
}
