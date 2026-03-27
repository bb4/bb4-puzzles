// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.ui

import org.scalatest.funsuite.AnyFunSuite

/**
  * Regression: final status must report the number of moves in the solution path, not Option.size (0 or 1).
  */
class PuzzleViewerFinalStatusSuite extends AnyFunSuite {

  private final class TestPuzzleViewer extends PuzzleViewer[Int, String] {
    override protected def createStatusMessage(numTries: Long): String = ""

    override def animateTransition(trans: String): Int = 0

    def exposeCreateFinalStatusMessage(
        numTries: Long,
        millis: Long,
        path: Option[Seq[String]]
    ): String =
      createFinalStatusMessage(numTries, millis, path)
  }

  test("createFinalStatusMessage uses path length not Option.size") {
    val v = new TestPuzzleViewer()
    val msg = v.exposeCreateFinalStatusMessage(0L, 1000L, Some(Seq("a", "b", "c")))
    assert(msg.startsWith("Found solution with 3 steps in "), s"unexpected message: $msg")
  }

  test("createFinalStatusMessage when no path") {
    val v = new TestPuzzleViewer()
    assertResult("Did not find solution.") {
      v.exposeCreateFinalStatusMessage(0L, 0L, None)
    }
  }
}
