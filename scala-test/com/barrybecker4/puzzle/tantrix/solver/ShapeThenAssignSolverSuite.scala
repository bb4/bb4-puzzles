// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.{FixedStartTantrixController, StubTantrixRefreshable, TantrixTstUtil}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class ShapeThenAssignSolverSuite extends AnyFunSuite {

  private val ui = new StubTantrixRefreshable

  private def applyMoves(board: TantrixBoard, moves: Seq[TilePlacement]): TantrixBoard =
    moves.foldLeft(board)(_.placeTile(_))

  private def shuffledStart(numTiles: Int, seed: Int) =
    new TantrixBoard(new Random(seed).shuffle(TILES.createOrderedList(numTiles)))

  private def solveAndCheck(start: TantrixBoard, seed: Int): Unit = {
    val controller = new FixedStartTantrixController(ui, start)
    val moves = new ShapeThenAssignSolver(controller, rnd = new Random(seed)).solve
    assert(moves.isDefined, s"Expected a solution for ${start.numTiles} tiles with seed $seed")
    val end = applyMoves(start, moves.get)
    assert(controller.isGoal(end))
    assert(end.unplacedTiles.isEmpty)
    assertResult(start.numTiles)(end.tantrix.size)
  }

  test("solves puzzles of every size from three to twelve tiles") {
    for (numTiles <- 3 to 12; seed <- 1 to 5) solveAndCheck(shuffledStart(numTiles, seed), seed)
  }

  test("solves puzzles of twenty, twenty-four and thirty tiles") {
    for (numTiles <- Seq(20, 24, 30); seed <- 1 to 3) solveAndCheck(shuffledStart(numTiles, seed), seed)
  }

  test("a puzzle that starts with more than one tile placed is solved by the fallback search") {
    val start = TantrixTstUtil.place2of3Tiles_OneThenTwo
    val controller = new FixedStartTantrixController(ui, start)
    val moves = new ShapeThenAssignSolver(controller).solve
    assert(moves.isDefined)
    assert(controller.isGoal(applyMoves(start, moves.get)))
  }

  test("there is no solution if some tile does not have the color of the loop") {
    // after the first 14 tiles, the tiles do not all have the color of the last tile
    val start = shuffledStart(32, 1)
    assert(!ShapeAssignSearch.allHavePath(start.tantrix.tiles.head.tile +: start.unplacedTiles, start.primaryColor))
    assertResult(None)(new ShapeThenAssignSolver(new FixedStartTantrixController(ui, start)).solve)
  }

  test("an attempt that runs out of steps says so, and another attempt can still succeed") {
    val start = shuffledStart(24, 2)
    val first = start.tantrix.tiles.head
    val search = new ShapeAssignSearch(first.tile +: start.unplacedTiles.toIndexedSeq, start.primaryColor, first, new Random(2))
    assertResult(ShapeAssignSearch.OutOfSteps)(search.run(3))
    var outcome: ShapeAssignSearch.Outcome = ShapeAssignSearch.OutOfSteps
    var attempts = 0
    while (outcome == ShapeAssignSearch.OutOfSteps && attempts < 1000) { outcome = search.run(1000); attempts += 1 }
    outcome match {
      case ShapeAssignSearch.Found(moves) => assert(applyMoves(start, moves).isSolved)
      case other => fail("Expected a solution, but got " + other)
    }
  }

  test("the search is repeatable for a given seed") {
    val start = shuffledStart(20, 4)
    val controller = new FixedStartTantrixController(ui, start)
    val a = new ShapeThenAssignSolver(controller, rnd = new Random(9)).solve
    val b = new ShapeThenAssignSolver(controller, rnd = new Random(9)).solve
    assertResult(a)(b)
  }

  test("the budget must be positive") {
    assertThrows[IllegalArgumentException](
      new ShapeThenAssignSolver(new FixedStartTantrixController(ui, shuffledStart(5, 1)), baseBudget = 0))
  }
}
