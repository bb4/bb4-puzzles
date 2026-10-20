// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.{TantrixBoard, TilePlacement}
import com.barrybecker4.puzzle.tantrix.{FixedStartTantrixController, StubTantrixRefreshable, TantrixTstUtil}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class RestartingDepthFirstSolverSuite extends AnyFunSuite {

  private val ui = new StubTantrixRefreshable

  private def applyMoves(board: TantrixBoard, moves: Seq[TilePlacement]): TantrixBoard =
    moves.foldLeft(board)(_.placeTile(_))

  private def shuffledStart(numTiles: Int, seed: Int) =
    new TantrixBoard(new Random(seed).shuffle(TILES.createOrderedList(numTiles)))

  test("the Luby sequence limits restarts to 1, 1, 2, 1, 1, 2, 4, 1, 1, 2, 1, 1, 2, 4, 8") {
    assertResult(List(1, 1, 2, 1, 1, 2, 4, 1, 1, 2, 1, 1, 2, 4, 8))(
      (1 to 15).map(RestartingDepthFirstSolver.luby).toList)
    assertResult(16)(RestartingDepthFirstSolver.luby(31))
    assertResult(1)(RestartingDepthFirstSolver.luby(32))
  }

  test("the Luby sequence starts at one") {
    assertThrows[IllegalArgumentException](RestartingDepthFirstSolver.luby(0))
  }

  test("finds the move that completes a loop from two of three tiles") {
    val start = TantrixTstUtil.place2of3Tiles_OneThenTwo
    val controller = new FixedStartTantrixController(ui, start)
    val moves = new RestartingDepthFirstSolver(controller).solve
    assert(moves.isDefined)
    assertResult(1)(moves.get.size)
    assert(controller.isGoal(applyMoves(start, moves.get)))
  }

  test("solves the six tile puzzle from the three of six fixture") {
    val start = TantrixTstUtil.place3of6UnsolvedTiles
    val controller = new FixedStartTantrixController(ui, start)
    val moves = new RestartingDepthFirstSolver(controller).solve
    assert(moves.isDefined)
    val end = applyMoves(start, moves.get)
    assert(controller.isGoal(end))
    assert(end.unplacedTiles.isEmpty)
  }

  test("solves puzzles of ten and fourteen tiles") {
    for (numTiles <- Seq(10, 14)) {
      val start = shuffledStart(numTiles, 1)
      val controller = new FixedStartTantrixController(ui, start)
      val moves = new RestartingDepthFirstSolver(controller, rnd = new Random(1)).solve
      assert(moves.isDefined, s"Expected a solution for $numTiles tiles")
      assert(controller.isGoal(applyMoves(start, moves.get)))
    }
  }

  test("still finds a solution when the attempts are very short, because the limits grow") {
    val start = shuffledStart(8, 2)
    val controller = new FixedStartTantrixController(ui, start)
    val solver = new RestartingDepthFirstSolver(controller, baseBudget = 1, rnd = new Random(3))
    val moves = solver.solve
    assert(moves.isDefined)
    assert(controller.isGoal(applyMoves(start, moves.get)))
    assert(solver.numRestarts > 0, "A budget of one step should not be enough for the first attempt")
  }

  test("returns no solution when the search finishes without finding one") {
    val start = TantrixTstUtil.place4UnsolvedTiles
    val controller = new FixedStartTantrixController(ui, start)
    assert(new RestartingDepthFirstSolver(controller).solve.isEmpty)
  }

  test("proves there is no solution even when it takes several attempts") {
    // There is no loop of all of the tiles when the path is already closed with a tile left over.
    val solved = TantrixTstUtil.place3SolvedTiles
    val withLeftover = new TantrixBoard(solved.tantrix, solved.primaryColor, Seq(TILES.getTile(4)), 4)
    val controller = new FixedStartTantrixController(ui, withLeftover)
    val solver = new RestartingDepthFirstSolver(controller, baseBudget = 1)
    assert(solver.solve.isEmpty)
  }
}
