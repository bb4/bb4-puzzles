// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.i18n.StubMessageContext
import com.barrybecker4.puzzle.bridge.model._

import scala.collection.Seq
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.scalatest.{BeforeAndAfterAll, FunSuite}


/**
  * These are more integration tests than unit tests.
  *
  * @author Barry Becker
  */
class SolvingSuite extends FunSuite with BeforeAndAfterAll {


  override def beforeAll() {
    AppContext.injectMessageContext(new StubMessageContext)
  }

  override def afterAll() {
    AppContext.injectMessageContext(null)
  }

  test("Solving tests with AStar sequential") {
    runSolvingTests(A_STAR_SEQUENTIAL)
  }

  test("Solving tests with AStar concurrent") {
    runSolvingTests(A_STAR_CONCURRENT)
  }

  /** One case failing and needs to be fixed */
  test("Solving tests with concurrent optimum") {
    runSolvingTests(CONCURRENT_OPTIMUM)
  }

 /** One case failing and needs to be fixed */
  test("Solving tests with sequential search") {
    runSolvingTests(SIMPLE_SEQUENTIAL)
  }

  @throws[Exception]
  private def runSolvingTests(algorithm: Algorithm) {
    val controller = new BridgePuzzleController(null)
    for (testCase <- InitialConfiguration.CONFIGURATIONS) {
      controller.setConfiguration(testCase.peopleSpeeds)
      val solver = algorithm.createSolver(controller)
      System.out.println("initial pos = " + controller.initialState)
      val path = solver.solve
      assertNotNull("No solution found for case: " + testCase.getLabel, path)
      val msg = "Unexpected minimum amount of time to cross for (" + testCase.getLabel + ") " +
        "for " + algorithm.getLabel + ". The path was " + path + ". "
      assertEquals(msg + "path length =" + path.get.size, testCase.shortestPath, pathCost(path.get))
    }
  }

  private def pathCost(path: Seq[BridgeMove]) = path.map(_.cost).sum
}
