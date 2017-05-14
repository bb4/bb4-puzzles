// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import org.junit.Assert.{assertFalse, assertTrue}
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class SolutionVerifierSuite extends FunSuite {
  /** instance under test */
  private var verifier: SolutionVerifier = _

  test("3TilesIsNotSolved") {
    verifier = new SolutionVerifier(place3UnsolvedTiles)
    assertFalse("Unexpectedly solved", verifier.isSolved)
  }

  test("3NonPathTilesIsNotSolved") {
    verifier = new SolutionVerifier(place3NonPathTiles)
    assertFalse("Unexpectedly solved", verifier.isSolved)
  }

  test("3TilesIsNotSolved2") {
    verifier = new SolutionVerifier(place3UnsolvedTiles2)
    assertFalse("Unexpectedly solved", verifier.isSolved)
  }

  test("3TilesIsSolved") {
    verifier = new SolutionVerifier(place3SolvedTiles)
    assertTrue("Unexpectedly not solved", verifier.isSolved)
  }

  test("4TilesIsNotSolved") {
    verifier = new SolutionVerifier(place4UnsolvedTiles)
    assertFalse("Unexpectedly solved", verifier.isSolved)
  }

  test("4TilesIsSolved") {
    verifier = new SolutionVerifier(place4SolvedTiles)
    assertTrue("Unexpectedly not solved", verifier.isSolved)
  }

  test("10TilesWithSpacesIsNotSolved") {
    verifier = new SolutionVerifier(place10LoopWithInnerSpace)
    assertFalse("Unexpectedly solved", verifier.isSolved)
  }
}