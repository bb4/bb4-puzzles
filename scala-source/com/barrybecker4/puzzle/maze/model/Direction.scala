package com.barrybecker4.puzzle.maze.model

import com.barrybecker4.common.geometry.IntLocation
import com.barrybecker4.common.geometry.Location

/**
  * Possible directions that we can go.
  * Vary the probability that each direction occurs for interesting effects.
  * The sum of these probabilities must sum to 1.
  *
  * @author Barry Becker
  */
object Direction {
  val VALUES = Seq(FORWARD, LEFT, RIGHT)
}

abstract class Direction {

  def apply(dir: Location): Location

  /** Find the direction which is counterclockwise 90 (to the left) of the specified dir. */
  protected def leftOf(dir: Location): Location = {
    if (dir.getX == 0) new IntLocation(0, if (dir.getY > 0) -1 else 1)
    else new IntLocation(if (dir.getX > 0) 1 else -1, 0)
  }

  /** Find the direction which is clockwise 90 (to the right) of the specified dir. */
  protected def rightOf(dir: Location): Location = {
    if (dir.getX == 0)  new IntLocation(0, if (dir.getY > 0) 1 else -1)
    else new IntLocation(if (dir.getX > 0) -1 else 1, 0)
  }
}


case object FORWARD extends Direction {
  override def apply(dir: Location): Location = dir
}

case object LEFT extends Direction {
  override def apply(dir: Location): Location = leftOf(dir)
}

case object RIGHT extends Direction {
  override def apply(dir: Location): Location = rightOf(dir)
}
