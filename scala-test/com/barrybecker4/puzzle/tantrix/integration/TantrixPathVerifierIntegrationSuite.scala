// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.integration

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import com.barrybecker4.puzzle.tantrix.model.{PathColor, TantrixBoard}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import com.barrybecker4.puzzle.tantrix.solver.verification.{InnerSpaceDetector, LoopDetector, SolutionVerifier}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * Combines path construction, loop detection, inner-hole detection, and solution verification
  * on hand-crafted boards and paths.
  */
class TantrixPathVerifierIntegrationSuite extends AnyFunSuite {

  test("solvedSevenTileBoardNoInnerSpacesAndVerifierPasses") {
    val b = place7SolvedTiles
    assert(new InnerSpaceDetector(b.tantrix).numInnerSpaces() == 0)
    assert(new LoopDetector(b).hasLoop)
    assert(new SolutionVerifier(b).isSolved)
  }

  test("tenTileLoopWithHoleHasInnerSpaceAndFailsVerifier") {
    val b = place10LoopWithInnerSpace
    assert(new InnerSpaceDetector(b.tantrix).numInnerSpaces() >= 1)
    assert(!new SolutionVerifier(b).isSolved)
  }

  test("pathUtilLoopPathsYieldLoopDetectorTrueOnBoard") {
    val cases = Seq(
      (PathTstUtil.LOOP_PATH3, PathColor.YELLOW),
      (PathTstUtil.LOOP_PATH4, PathColor.RED),
      (PathTstUtil.LOOP_PATH6, PathColor.BLUE)
    )
    cases.foreach { case (path, color) =>
      val board = new TantrixBoard(path.tiles, color)
      assert(new LoopDetector(board).hasLoop,
        s"LoopDetector expected loop for ${path.tiles.size}-tile path at $color")
    }
  }

  test("randomPermutedPathStillVerifiesPrimaryOrdering") {
    val board = place4SolvedTiles
    val path = new TantrixPath(board.tantrix, board.primaryColor, board.numTiles, new Random(99))
    assert(TantrixPath.hasOrderedPrimaryPath(path.tiles, board.primaryColor))
    val fitness = com.barrybecker4.puzzle.tantrix.solver.path.PathEvaluator().evaluateFitness(path)
    assert(fitness == 0.0,
      s"Fully solved board path should score 0, got $fitness")
  }
}
