// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.common.geometry.Location
import Direction._


/**
  * Possible directions that we can go.
  * Vary the probability that each direction occurs for interesting effects.
  * The sum of these probabilities must sum to 1.
  * @author Barry Becker
  */
enum Direction {
  case FORWARD extends Direction
  case LEFT extends Direction
  case RIGHT extends Direction

  def apply(dir: Location): Location = this match {
    case FORWARD => dir
    case LEFT => leftOf(dir)
    case RIGHT => rightOf(dir)
  }

  private def leftOf(dir: Location): Location = {
    if (dir.getX == 0) IntLocation(0, if (dir.getY > 0) -1 else 1)
    else IntLocation(if (dir.getX > 0) 1 else -1, 0)
  }

  private def rightOf(dir: Location): Location = {
    if (dir.getX == 0) IntLocation(0, if (dir.getY > 0) 1 else -1)
    else IntLocation(if (dir.getX > 0) -1 else 1, 0)
  }

  def VALUES: Seq[Direction] = Seq(FORWARD, LEFT, RIGHT)
}