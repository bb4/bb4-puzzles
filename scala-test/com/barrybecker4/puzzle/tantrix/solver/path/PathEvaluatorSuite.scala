package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import org.scalatest.funsuite.AnyFunSuite


class PathEvaluatorSuite extends AnyFunSuite {

  private val evaluator = PathEvaluator()

  test("PathEvaluator for LOOP_PATH3") {
    assertResult( 0.0) {
      evaluator.evaluateFitness(LOOP_PATH3)
    }
  }
  test("PathEvaluator for NON_LOOP_PATH3") {
    assertResult( 3.5032258064516135) {
      evaluator.evaluateFitness(NON_LOOP_PATH3)
    }
  }

  test("PathEvaluator for LOOP_PATH4") {
    assertResult(0.0) {
      evaluator.evaluateFitness(LOOP_PATH4)
    }
  }

  test("PathEvaluator for NON_LOOP_PATH4") {
    assertResult( 3.326829268292683) {
      evaluator.evaluateFitness(NON_LOOP_PATH4)
    }
  }

}
