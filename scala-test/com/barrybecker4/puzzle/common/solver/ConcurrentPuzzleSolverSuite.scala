package com.barrybecker4.puzzle.common.solver

import org.scalatest.funsuite.AnyFunSuite

/**
  * Verifies the concurrent solver completes with [[None]] when no solution exists (no [[Some(null)]]).
  */
class ConcurrentPuzzleSolverSuite extends AnyFunSuite {

  test("no legal moves yields None without throwing") {
    val controller = new DeadEndTestController
    val solver = new ConcurrentPuzzleSolver[Int, String](controller, depthBreadthFactor = 0.5f)
    val result = solver.solve
    assert(result.isEmpty)
  }
}
