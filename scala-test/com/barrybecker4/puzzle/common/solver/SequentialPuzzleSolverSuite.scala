// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import org.scalatest.funsuite.AnyFunSuite

class SequentialPuzzleSolverSuite extends AnyFunSuite {

  /** Satisfies `PuzzleController` initialization; solver tests call `SequentialPuzzleSolver` directly. */
  private object DummyAlgorithm extends AlgorithmEnum[Int, String] {
    def getLabel: String = "dummy"
    def ordinal: Int = 0
    def createSolver(c: PuzzleController[Int, String]): PuzzleSolver[String] =
      SequentialPuzzleSolver(c)
  }

  /** Linear state space: 0 → 1 → 2 with a single move label. */
  private class LinePuzzleController extends AbstractPuzzleController[Int, String](null) {
    algorithm = DummyAlgorithm
    override def initialState: Int = 0
    override def isGoal(position: Int): Boolean = position == 2
    override def legalTransitions(position: Int): Seq[String] =
      if position < 2 then Seq("inc") else Seq.empty
    override def transition(position: Int, move: String): Int = position + 1
    override def animateTransition(trans: String): Int =
      throw new UnsupportedOperationException("not used in solver tests")
  }

  test("finds path on tiny linear puzzle") {
    val c = new LinePuzzleController
    val solver = new SequentialPuzzleSolver[Int, String](c)
    assertResult(Some(Seq("inc", "inc")))(solver.solve)
  }
}
