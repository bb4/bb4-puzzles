// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.testsupport.i18n.StubMessageContext
import com.barrybecker4.puzzle.redpuzzle.{RedPuzzleController, StubRefreshable}
import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceList, PieceLists}
import com.barrybecker4.search.Refreshable
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class FastBacktrackingSolverSuite extends AnyFunSuite {

  AppContext.injectMessageContext(new StubMessageContext)

  private def assertValidSolution(result: Option[Seq[OrientedPiece]], numTotal: Int): Unit = {
    val placed = result.getOrElse(fail("expected a solution"))
    assertResult(numTotal)(placed.size)
    placed.indices.foreach(i => assert(PieceList(placed.take(i).toList, numTotal).fits(placed(i)), s"cell $i"))
  }

  test("solves the 9 piece puzzle for many shuffles") {
    val controller = new RedPuzzleController(new StubRefreshable)
    for (seed <- 0 until 25)
      assertValidSolution(new FastBacktrackingSolver(controller, PieceLists.getInitialPuzzlePieces(9, new Random(seed))).solve, 9)
  }

  test("solves the 4 piece puzzle") {
    val controller = new RedPuzzleController(new StubRefreshable)
    assertValidSolution(new FastBacktrackingSolver(controller, PieceLists.getInitialPuzzlePieces(4, new Random(1))).solve, 4)
  }

  test("uses every one of the puzzle's pieces") {
    val controller = new RedPuzzleController(new StubRefreshable)
    val result = new FastBacktrackingSolver(controller, PieceLists.getInitialPuzzlePieces(9, new Random(2))).solve
    assertResult((1 to 9).toList)(result.get.map(_.piece.pieceNumber).sorted.toList)
  }

  test("keeps the solution and the number of tries it needed") {
    val solver = new FastBacktrackingSolver(new RedPuzzleController(new StubRefreshable),
      PieceLists.getInitialPuzzlePieces(9, new Random(4)))
    val result = solver.solve
    assertResult(result.get.toList)(solver.solution.pieces)
    assert(solver.numTries >= 9)
  }

  test("tells the puzzle about the final result exactly once") {
    val ui = new CountingRefreshable
    new FastBacktrackingSolver(new RedPuzzleController(ui), PieceLists.getInitialPuzzlePieces(9, new Random(6))).solve
    assertResult(1)(ui.finalCount)
    assert(ui.lastFinalPath.exists(_.size == 9))
  }

  private class CountingRefreshable extends Refreshable[PieceList, OrientedPiece] {
    var finalCount = 0
    var lastFinalPath: Option[Seq[OrientedPiece]] = None
    override def refresh(state: PieceList, numTries: Long): Unit = {}
    override def animateTransition(transition: OrientedPiece): PieceList = null
    override def finalRefresh(path: Option[Seq[OrientedPiece]], state: Option[PieceList],
                              numTries: Long, elapsedMillis: Long): Unit = {
      finalCount += 1
      lastFinalPath = path
    }
  }
}
