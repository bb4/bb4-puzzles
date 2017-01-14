package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.Location

/**
  * The state space position, depth, and direction while searching.
  * Immutable.
  * @param position      current position
  * @param movement movement to make relative to current position.
  * @param depth    depth in the search
  * @author Barry Becker
  */
case class GenState(position: Location, movement: Location, depth: Int) {

  /** The amount to move relative to the current position */
  def getRelativeMovement: Location = movement

  override def toString: String = "[pos=" + position + " move=" + movement + " depth=" + depth + "]"
}


