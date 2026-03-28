// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.integration

import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import com.barrybecker4.puzzle.tantrix.{FixedStartTantrixController, StubTantrixRefreshable, TantrixTstUtil}
import org.scalatest.funsuite.AnyFunSuite

/**
  * End-to-end DFS: controller move generation + transitions must reach [[TantrixController.isGoal]] from a near-complete state.
  */
class TantrixSequentialSearchIntegrationSuite extends AnyFunSuite {

  private val ui = new StubTantrixRefreshable

  private def applyMoves(board: TantrixBoard, moves: Seq[TilePlacement]): TantrixBoard =
    moves.foldLeft(board)(_.placeTile(_))

  test("sequentialSolverFinishesOneMoveFromTwoOfThree") {
    val start = TantrixTstUtil.place2of3Tiles_OneThenTwo
    val controller = new FixedStartTantrixController(ui, start)
    val solver = new SequentialPuzzleSolver(controller)
    val maybeMoves = solver.solve
    assert(maybeMoves.isDefined, "Expected a solution from one tile away from complete")
    val moves = maybeMoves.get
    assert(moves.size == 1)
    val end = applyMoves(start, moves)
    assert(controller.isGoal(end))
  }

  test("sequentialSolverSolvesThreeTilePuzzleFromOnePlaced") {
    val tiles = com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES.createOrderedList(3)
    val start = new TantrixBoard(tiles)
    val controller = new FixedStartTantrixController(ui, start)
    val solver = new SequentialPuzzleSolver(controller)
    val maybeMoves = solver.solve
    assert(maybeMoves.isDefined, "DFS should solve 3-tile puzzle from initial single tile")
    val end = applyMoves(start, maybeMoves.get)
    assert(controller.isGoal(end))
    assert(end.unplacedTiles.isEmpty)
  }

  test("sequentialSolverFromThreeOfSixPartial") {
    val start = TantrixTstUtil.place3of6UnsolvedTiles
    assert(start.unplacedTiles.nonEmpty)
    val controller = new FixedStartTantrixController(ui, start)
    val solver = new SequentialPuzzleSolver(controller)
    val maybeMoves = solver.solve
    assert(maybeMoves.isDefined, "DFS should complete 6-tile puzzle from mid-game fixture")
    val end = applyMoves(start, maybeMoves.get)
    assert(controller.isGoal(end))
    assert(end.unplacedTiles.isEmpty)
  }
}
