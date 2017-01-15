package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.Location

import scala.collection.mutable


/**
  * Stack of GenStates to try during the search.
  * @param probabilities the probabilities of moving each direction.
  * @author Barry Becker
  */
class StateStack(val probabilities: Probabilities = Probabilities(1.0, 1.0, 1.0)) {

  // Push and pop from the beginning of the list
  private var stack: List[GenState] = List()

  // +:= to prepend, :+= to append
  def push(state: GenState): Unit = stack +:= state

  def pop(): GenState = {
    val h = stack.head
    stack = stack.tail
    h
  }

  def peek(): GenState = stack.head
  def isEmpty: Boolean = stack.isEmpty
  def clear(): Unit = { stack = List() }

  /**
    * From currentPosition, try moving in each direction in a random order.
    * Assigning different probabilities to the order in which we check these directions
    * can give interesting effects.
    */
  def pushMoves(currentPosition: Location, currentDir: Location, depth: Int) {
    val directions = probabilities.getShuffledDirections
    // check all the directions except the one we came from
    for (i <- 0 until 3) {
      val direction = directions(i)
      val movement = direction(currentDir)
      push(GenState(currentPosition, movement, depth))
    }
  }
}
