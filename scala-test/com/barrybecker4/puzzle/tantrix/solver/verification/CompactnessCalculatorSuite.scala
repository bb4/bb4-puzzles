package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.PathTstUtil
import com.barrybecker4.puzzle.tantrix.solver.verification
import org.scalatest.funsuite.AnyFunSuite


class CompactnessCalculatorSuite extends AnyFunSuite {

  /** instance under test */
  private val calculator = CompactnessCalculator()

  test("Compactness of paths") {

    for (testCase <- pathVerificationCases) {
      val compactness = calculator.determineCompactness(testCase.path)
      assertResult(testCase.compactness, s"expected compactness = ${testCase.compactness} but got ${compactness} for ${testCase.path}") {
        compactness
      }
    }
  }
}
