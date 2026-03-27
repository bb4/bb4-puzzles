package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.solver.path.PathVerificationCase
import org.scalatest.funsuite.AnyFunSuite


class CompactnessCalculatorSuite extends AnyFunSuite {

  /** instance under test */
  private val calculator = CompactnessCalculator()

  test("Compactness of paths") {

    var result: String = ""
    for (testCase <- PathVerificationCase.cases) {
      val compactness = calculator.determineCompactness(testCase.path)
      val equality = if (compactness == testCase.compactness) "matched" else s"!= $compactness"
      result += s"${testCase.name} = exp: ${testCase.compactness} $equality \n"
    }

    assert(!result.contains("!="))
  }
}
