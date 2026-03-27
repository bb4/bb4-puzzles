package com.barrybecker4.puzzle.redpuzzle.solver

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.testsupport.i18n.StubMessageContext
import com.barrybecker4.puzzle.redpuzzle.RedPuzzleController
import com.barrybecker4.puzzle.redpuzzle.model.PieceLists
import com.barrybecker4.puzzle.redpuzzle.StubRefreshable
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class BruteForceSolverSuite extends AnyFunSuite {

  AppContext.injectMessageContext(new StubMessageContext)

  test("finds a full solution for 9 pieces with fixed seed") {
    val rnd = new Random(42)
    val shuffled = PieceLists.getInitialPuzzlePieces(9, rnd)
    val controller = new RedPuzzleController(new StubRefreshable)
    val solver = new BruteForceSolver(controller, shuffled)
    val result = solver.solve
    assert(result.isDefined)
    assert(result.get.size == 9)
  }

  test("finds a full solution for 4 pieces") {
    val rnd = new Random(1)
    val shuffled = PieceLists.getInitialPuzzlePieces(4, rnd)
    val controller = new RedPuzzleController(new StubRefreshable)
    val solver = new BruteForceSolver(controller, shuffled)
    val result = solver.solve
    assert(result.isDefined)
    assert(result.get.size == 4)
  }
}
