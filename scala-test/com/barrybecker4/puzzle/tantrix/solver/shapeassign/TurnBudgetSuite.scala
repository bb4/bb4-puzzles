// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import org.scalatest.funsuite.AnyFunSuite

class TurnBudgetSuite extends AnyFunSuite {

  private val tiles = new LoopTiles(TILES.createOrderedList(6).toIndexedSeq, TILES.getTile(6).primaryColor)
  private val budget = new TurnBudget(tiles)
  budget.reset()

  test("the budget starts with the tiles after the first one") {
    assertResult(5)((0 until LoopKind.NUM_KINDS).map(tiles.numberOfKind).sum)
    assert((0 until LoopKind.NUM_KINDS).forall(kind => budget.isAvailable(kind) == (tiles.numberOfKind(kind) > 0)))
  }

  test("using up the tiles of a kind makes the kind unavailable, and giving one back makes it available again") {
    val kind = (0 until LoopKind.NUM_KINDS).find(tiles.numberOfKind(_) > 0).get
    for (_ <- 0 until tiles.numberOfKind(kind)) budget.use(kind)
    assert(!budget.isAvailable(kind))
    budget.giveBack(kind)
    assert(budget.isAvailable(kind))
    budget.reset()
  }

  test("a full turn needs more turning than the cells that are left can give, or the wrong parity") {
    val wide = tiles.numberOfKind(LoopKind.WIDE_CURVE)
    val tight = tiles.numberOfKind(LoopKind.TIGHT_CURVE)
    assert(budget.canTurnBy(wide + 2 * tight))
    assert(budget.canTurnBy(-(wide + 2 * tight)))
    assert(!budget.canTurnBy(wide + 2 * tight + 2))
    assert(!budget.canTurnBy(wide + 2 * tight - 1))
  }

  test("the loop can be completed when the cells that are left can turn the rest of a full circle") {
    val wide = tiles.numberOfKind(LoopKind.WIDE_CURVE)
    val tight = tiles.numberOfKind(LoopKind.TIGHT_CURVE)
    val available = wide + 2 * tight
    assert(budget.canCompleteLoop(LoopKind.FULL_TURN - available))
    assert(!budget.canCompleteLoop(100))
  }
}
