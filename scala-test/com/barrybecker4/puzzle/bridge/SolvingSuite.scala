// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.i18n.StubMessageContext
import com.barrybecker4.puzzle.bridge.model._

import scala.collection.Seq
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import InitialConfiguration._

/**
  * These are more integration tests than unit tests.
  *
  * @author Barry Becker
  */
object SolvingSuite{

  /** initial states that have solutions */
  private val A_STAR_CASES = List(
    SolvingSuite.TestCase(TRIVIAL_PROBLEM, 8),
    SolvingSuite.TestCase(STANDARD_PROBLEM, 15),
    SolvingSuite.TestCase(ALTERNATIVE_PROBLEM, 60),
    SolvingSuite.TestCase(DIFFICULT_PROBLEM, 47),
    SolvingSuite.TestCase(BIG_PROBLEM, 126)
  )

  private case class TestCase(config: InitialConfiguration, expectedTimeToCross: Int)
}

class SolvingSuite extends FunSuite with BeforeAndAfterAll {


  override def beforeAll() {
    AppContext.injectMessageContext(new StubMessageContext)
  }

  override def afterAll() {
    AppContext.injectMessageContext(null)
  }

  test("Solving tests with AStar") {
    runSolvingTests(A_STAR_SEQUENTIAL, SolvingSuite.A_STAR_CASES)
  }


  @throws[Exception]
  private def runSolvingTests(algorithm: Algorithm, cases: List[SolvingSuite.TestCase]) {
    val controller = new BridgePuzzleController(null)
    for (testCase <- cases) {
      controller.setConfiguration(testCase.config.peopleSpeeds)
      val solver = algorithm.createSolver(controller)
      System.out.println("initial pos = " + controller.initialState)
      val path = solver.solve
      assertNotNull("No solution found for case: " + testCase.config.getLabel, path)
      val msg = "Unexpected minimum amount of time to cross for (" + testCase.config.getLabel + ") " +
        "for " + algorithm.getLabel + ". The path was " + path + ". "
      assertEquals(msg + "path length =" + path.get.size, testCase.expectedTimeToCross, pathCost(path.get))
    }
  }

  private def pathCost(path: Seq[BridgeMove]) = path.map(_.cost).sum
}
