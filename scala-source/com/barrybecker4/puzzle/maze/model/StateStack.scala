package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.Location

import scala.collection.mutable


/**
  * Stack of GenStates to try during the search.
  *
  * @author Barry Becker
  */
class StateStack {

  private var stack: List[GenState] = List()
  private[model] var probabilities = Probabilities(1.0, 1.0, 1.0)

  /** @param probs the probabilities of moving each direction. */
  def this(probs: Probabilities) {
    this()
    probabilities = probs
  }

  // +:= to prepend, :+= to append
  def push(state: GenState): Unit = stack +:= state

  def pop(): GenState = {
    val h = stack.head
    stack = stack.tail
    h
  }

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
