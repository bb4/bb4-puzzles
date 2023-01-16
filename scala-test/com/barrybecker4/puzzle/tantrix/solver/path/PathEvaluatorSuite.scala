// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import org.scalatest.funsuite.AnyFunSuite


class PathEvaluatorSuite extends AnyFunSuite {

  private val evaluator = PathEvaluator()

  test("Test path evaluation") {
    for (testCase <- PathVerificationCase.cases) {
      val fitness = evaluator.evaluateFitness(testCase.path)
      assertResult(testCase.fitness, s"expected fitness = ${testCase.fitness} but got $fitness for ${testCase.name}") {
        fitness
      }
    }
  }
  
}
