// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import org.scalatest.funsuite.AnyFunSuite

/**
  * [[TantrixBoard.isSolved]] and [[SolutionVerifier]] must agree; divergence indicates a logic bug in either path.
  */
class TantrixSolvedStateAgreementSuite extends AnyFunSuite {

  private def boards: Seq[TantrixBoard] = Seq(
    place3SolvedTiles,
    place3UnsolvedTiles,
    place3NonPathTiles,
    place3UnsolvedTiles2,
    place4SolvedTiles,
    place4UnsolvedTiles,
    place6UnsolvedTiles,
    place7SolvedTiles,
    place7LoopWrongColorTiles,
    place10LoopWithInnerSpace,
    place9AlmostLoop,
    placeJumbled9
  )

  test("isSolvedMatchesSolutionVerifier") {
    boards.foreach { b =>
      val v = new SolutionVerifier(b).isSolved
      assert(b.isSolved == v,
        s"isSolved (${b.isSolved}) != verifier ($v) for board primaryColor=${b.primaryColor}")
    }
  }
}
