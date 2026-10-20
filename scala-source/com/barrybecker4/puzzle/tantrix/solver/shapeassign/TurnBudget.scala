// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.solver.shapeassign.LoopKind.*

/**
  * Keeps track of how many tiles of each kind are still to be used. The cells of the loop must use all of the tiles,
  * so the number of cells of each kind has to be the same as the number of tiles of each kind. Also, the kinds
  * that are left limit how much more the loop can turn, and it has to turn a full circle in total.
  */
class TurnBudget(tiles: LoopTiles) {

  private val cellsLeft = new Array[Int](NUM_KINDS)

  /** Start over with all the tiles after the first one, which is already placed, to be used. */
  def reset(): Unit = (0 until NUM_KINDS).foreach(kind => cellsLeft(kind) = tiles.numberOfKind(kind))

  def isAvailable(kind: Int): Boolean = cellsLeft(kind) > 0
  def use(kind: Int): Unit = cellsLeft(kind) -= 1
  def giveBack(kind: Int): Unit = cellsLeft(kind) += 1

  /** @param headingSoFar the total turn of the cells so far, in steps of 60 degrees
    * @return true if the cells that are left can bring the total to a full turn, one way or the other
    */
  def canCompleteLoop(headingSoFar: Int): Boolean =
    canTurnBy(FULL_TURN - headingSoFar) || canTurnBy(-FULL_TURN - headingSoFar)

  /** Each wide curve turns by one step and each tight curve by two, in either direction. */
  def canTurnBy(amount: Int): Boolean = {
    val wide = cellsLeft(WIDE_CURVE)
    (0 to wide).exists(numTurningBack => canTurnByTightCurves(amount - (wide - 2 * numTurningBack)))
  }

  private def canTurnByTightCurves(amount: Int): Boolean = {
    val tight = cellsLeft(TIGHT_CURVE)
    Math.abs(amount) <= 2 * tight && Math.floorMod(2 * tight - amount, 4) == 0
  }
}
