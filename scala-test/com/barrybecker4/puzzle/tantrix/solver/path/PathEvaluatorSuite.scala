package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil._
import org.scalatest.funsuite.AnyFunSuite


class PathEvaluatorSuite extends AnyFunSuite {

  private val evaluator = PathEvaluator()

  

  test("PathEvaluator for LOOP_PATH3") {
    assertResult(0.0) {
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

  test("PathEvaluator for NON_LOOP_PATH5") {
    assertResult(3.3626998843382534) {
      evaluator.evaluateFitness(NON_LOOP_PATH5)
    }
  }

  test("PathEvaluator for COMPACT_NON_LOOP_PATH5") {
    assertResult(3.3626998843382534) {
      evaluator.evaluateFitness(COMPACT_NON_LOOP_PATH5)
    }
  }

  test("PathEvaluator for LINEAR_NON_LOOP_PATH5") {
    assertResult(2.787908748502118) {
      evaluator.evaluateFitness(LINEAR_NON_LOOP_PATH5)
    }
  }

  test("PathEvaluator for LOOP_PATH5") {
    assertResult(0) {
      evaluator.evaluateFitness(LOOP_PATH5)
    }
  }

}
