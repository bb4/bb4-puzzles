package com.barrybecker4.puzzle.maze.model


/**
  * @author Barry Becker
  */
class DistributionMap(forwardDist: List[Int], leftDist: List[Int], rightDist: List[Int]) {

  private var map = Map[Direction, List[Int]]()
  map += FORWARD -> forwardDist
  map += LEFT -> leftDist
  map += RIGHT -> rightDist

  def this() = {
    this(List(0, 0, 0), List(0, 0, 0), List(0, 0, 0))
  }

  def increment(directions: List[Direction]): Unit = {
    increment(directions(0), 0)
    increment(directions(1), 1)
    increment(directions(2), 2)
  }

  private def increment(dir: Direction, position: Int): Unit = {
    val list = map(dir)
    map += dir -> list.updated(position, list(position) + 1)
    //map += dir -> list.patch(position, Seq(list(position) + 1), 1
  }
}
