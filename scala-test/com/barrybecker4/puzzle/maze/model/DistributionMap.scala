package com.barrybecker4.puzzle.maze.model

import scala.collection.mutable



/**
  * @author Barry Becker
  */
class DistributionMap(forwardDist: List[Int], leftDist: List[Int], rightDist: List[Int])
            extends mutable.HashMap[Direction, List[Int]] {

  this(FORWARD) = forwardDist
  this(LEFT) = leftDist
  this(RIGHT) = rightDist

  def this() {
    this(List(0, 0, 0), List(0, 0, 0), List(0, 0, 0))
  }

  def increment(directions: List[Direction]) {
    increment(directions(0), 0)
    increment(directions(1), 1)
    increment(directions(2), 2)
  }

  private def increment(dir: Direction, position: Int) {
    val list = this(dir)
    this(dir) = list.updated(position, list(position) + 1)
    //this(dir) = list.patch(position, Seq(list(position) + 1), 1)
  }
}
