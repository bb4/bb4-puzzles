// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.verification

import scala.compiletime.uninitialized
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import org.junit.jupiter.api.Assertions.{assertFalse, assertTrue}
import org.scalatest.funsuite.AnyFunSuite

/**
  */
class SolutionVerifierSuite extends AnyFunSuite {
  /** instance under test */
  private var verifier: SolutionVerifier = uninitialized

  test("3TilesIsNotSolved") {
    verifier = new SolutionVerifier(place3UnsolvedTiles)
    assertFalse(verifier.isSolved, "Unexpectedly solved")
  }

  test("3NonPathTilesIsNotSolved") {
    verifier = new SolutionVerifier(place3NonPathTiles)
    assertFalse(verifier.isSolved, "Unexpectedly solved")
  }

  test("3TilesIsNotSolved2") {
    verifier = new SolutionVerifier(place3UnsolvedTiles2)
    assertFalse(verifier.isSolved, "Unexpectedly solved")
  }

  test("3TilesIsSolved") {
    verifier = new SolutionVerifier(place3SolvedTiles)
    assertTrue(verifier.isSolved, "Unexpectedly not solved")
  }

  test("4TilesIsNotSolved") {
    verifier = new SolutionVerifier(place4UnsolvedTiles)
    assertFalse(verifier.isSolved, "Unexpectedly solved")
  }

  test("4TilesIsSolved") {
    verifier = new SolutionVerifier(place4SolvedTiles)
    assertTrue(verifier.isSolved, "Unexpectedly not solved")
  }

  test("7TilesIsSolved") {
    verifier = new SolutionVerifier(place7SolvedTiles)
    assertTrue(verifier.isSolved, "Unexpectedly not solved")
  }

  test("7TilesWongColorIsNotSolved") {
    verifier = new SolutionVerifier(place7LoopWrongColorTiles)
    assertFalse(verifier.isSolved, "Unexpectedly solved")
  }

  test("10TilesWithSpacesIsNotSolved") {
    verifier = new SolutionVerifier(place10LoopWithInnerSpace)
    assertFalse(verifier.isSolved, "Unexpectedly solved")
  }
}