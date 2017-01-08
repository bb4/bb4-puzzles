package com.barrybecker4.puzzle.bridge

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.i18n.StubMessageContext
import com.barrybecker4.puzzle.bridge.model._

import scala.collection.JavaConverters
import scala.collection.Seq
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.scalatest.{BeforeAndAfterAll, FunSuite}

/**
  * These are more integration tests than unit tests.
  *
  * @author Barry Becker
  */
object SolvingSuite{

  /** initial states that have solutions */
  private val A_STAR_CASES = List(
    new SolvingSuite.TestCase(TRIVIAL_PROBLEM, 8),
    new SolvingSuite.TestCase(STANDARD_PROBLEM, 15),
    new SolvingSuite.TestCase(ALTERNATIVE_PROBLEM, 60),
    new SolvingSuite.TestCase(DIFFICULT_PROBLEM, 47)
  )


  private class TestCase(var config: InitialConfiguration, var expectedTimeToCross: Int)
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

  /** these will not find the shortest path
    *
    * public void testSolvingSimpleSequentialTests() throws Exception {
    * runSolvingTests(Algorithm1.SIMPLE_SEQUENTIAL, SIMPLE_SEQUENTIAL_CASES);
    * }
    *
    * public void testSolvingConcurrentDepthTests() throws Exception {
    * runSolvingTests(Algorithm1.CONCURRENT_DEPTH, CONCURRENT_DEPTH_CASES);
    * }
    *
    * public void testSolvingConcurrentBreadthTests() throws Exception {
    * runSolvingTests(Algorithm1.CONCURRENT_BREADTH, CONCURRENT_BREADTH_CASES);
    * }
    */
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

  private def pathCost(path: Seq[BridgeMove]) = {
    var totalCost = 0
    for (move <- path) {
      totalCost += move.getCost
    }
    totalCost
  }
}
