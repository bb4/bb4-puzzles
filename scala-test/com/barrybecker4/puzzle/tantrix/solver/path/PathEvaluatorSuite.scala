// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import org.scalatest.funsuite.AnyFunSuite


class PathEvaluatorSuite extends AnyFunSuite {

  private val evaluator = PathEvaluator()

  test("Test path evaluation") {
    var result = ""
    for (testCase <- PathVerificationCase.cases) {
      val fitness = evaluator.evaluateFitness(testCase.path)
      val equality = if (fitness == testCase.fitness) "matched" else s"!= $fitness"
      result += s"${testCase.name} fitness = exp: ${testCase.fitness} $equality \n"
    }
    assert(!result.contains("!="))
  }
  
}
