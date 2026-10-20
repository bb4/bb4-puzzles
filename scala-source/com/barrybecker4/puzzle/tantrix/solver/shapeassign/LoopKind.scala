// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

/**
  * The kind of primary path that a cell of the loop (or a tile) has. It depends on how many sides apart the
  * two ends of the path are: 3 sides apart is straight, 2 or 4 is a wide curve, and 1 or 5 is a tight curve.
  * Going around the loop, each cell turns the heading by a number of steps of 60 degrees. Tight curves turn
  * it by 2 steps, wide curves by 1, and straights by 0. A loop that does not cross itself turns by [[FULL_TURN]] steps.
  */
object LoopKind {
  val STRAIGHT = 0
  val WIDE_CURVE = 1
  val TIGHT_CURVE = 2
  val NUM_KINDS = 3

  /** Steps of 60 degrees in a complete turn around the loop. */
  val FULL_TURN = 6

  /** @return how many sides it is from the entry side to the exit side, going around the tile. Always 1 to 5. */
  def sidesBetween(entrySide: Int, exitSide: Int): Int = Math.floorMod(exitSide - entrySide, 6)

  def kindOf(sidesBetween: Int): Int = sidesBetween match {
    case 3 => STRAIGHT
    case 2 | 4 => WIDE_CURVE
    case _ => TIGHT_CURVE
  }

  /** @return the change of heading in steps of 60 degrees. It is negative for turns one way and positive for the other. */
  def headingChange(sidesBetween: Int): Int = sidesBetween - 3
}
