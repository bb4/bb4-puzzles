// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common.solver

import com.barrybecker4.puzzle.common.{AlgorithmEnum, PuzzleController}
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController
import org.scalatest.funsuite.AnyFunSuite

class SequentialPuzzleSolverSuite extends AnyFunSuite {

  /** Satisfies `PuzzleController` initialization; solver tests call `SequentialPuzzleSolver` directly. */
  private enum DummyAlgorithm extends AlgorithmEnum[Int, String]:
    case TheOnly extends DummyAlgorithm
    def getLabel: String = "dummy"
    def createSolver(c: PuzzleController[Int, String]): PuzzleSolver[String] =
      SequentialPuzzleSolver(c)

  /** Linear state space: 0 → 1 → 2 with a single move label. */
  private class LinePuzzleController extends AbstractPuzzleController[Int, String](null) {
    algorithm = DummyAlgorithm.TheOnly
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

  /**
   * Regression for https://github.com/bb4/bb4-puzzles/issues/25 —
   * recursive DFS overflows the JVM stack on deep searches (e.g. 15-puzzle).
   * A long linear chain forces depth equal to goal without needing a real sliding board.
   */
  test("does not stack overflow on deep linear puzzle") {
    val depth = 20_000
    val c = new DeepLinePuzzleController(depth)
    val solver = new SequentialPuzzleSolver[Int, String](c)
    val path =
      try solver.solve
      catch {
        case _: StackOverflowError =>
          fail("DFS overflowed the stack; search must be iterative (issue #25)")
      }
    assert(path.isDefined, "expected a solution")
    assertResult(depth)(path.get.size)
    assert(path.get.forall(_ == "inc"))
  }

  /** Linear chain 0 → 1 → … → depth. */
  private class DeepLinePuzzleController(depth: Int) extends AbstractPuzzleController[Int, String](null) {
    algorithm = DummyAlgorithm.TheOnly
    override def initialState: Int = 0
    override def isGoal(position: Int): Boolean = position == depth
    override def legalTransitions(position: Int): Seq[String] =
      if position < depth then Seq("inc") else Seq.empty
    override def transition(position: Int, move: String): Int = position + 1
    override def animateTransition(trans: String): Int =
      throw new UnsupportedOperationException("not used in solver tests")
  }
}
