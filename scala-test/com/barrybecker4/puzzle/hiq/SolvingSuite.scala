// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.i18n.StubMessageContext
import org.junit.Assert.{assertEquals,assertTrue}
import org.scalatest.{BeforeAndAfter,  FunSuite}



class SolvingSuite extends FunSuite with BeforeAndAfter {

  before { AppContext.injectMessageContext(new StubMessageContext) }
  after { AppContext.injectMessageContext(null) }

  test("Solving tests with AStar concurrent") {
    runSolverTest(A_STAR_CONCURRENT)
  }

  test("Solving tests with AStar sequential") {
    runSolverTest(A_STAR_SEQUENTIAL)
  }

  test("Solving tests with Concurrent optimum") {
    runSolverTest(CONCURRENT_OPTIMUM)
  }

  test("Solving tests with simple sequential") {
    runSolverTest(SIMPLE_SEQUENTIAL)
  }

  @throws[Exception]
  private def runSolverTest(algorithm: Algorithm) {
    val controller = new HiQController(null)

    val start = System.currentTimeMillis()
    controller.setAlgorithm(algorithm)
    val solver = algorithm.createSolver(controller)

    System.out.println("initial pos = " + controller.initialState)
    val path = solver.solve
    assertTrue("No solution found for case: " + path, path.isDefined)

    println("time to solve = " + (System.currentTimeMillis() - start) + " millis")
    println("paths = " + path.get)

    val EXP_NUM_STEPS = 31 // shortest path
    assertEquals("Unexpected number of steps", EXP_NUM_STEPS, path.get.size)
  }

}
