// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle

import com.barrybecker4.puzzle.slidingpuzzle.solver.FastSliderSolver
import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.*
import com.barrybecker4.puzzle.slidingpuzzle.model.SliderBoard
import org.scalatest.funsuite.AnyFunSuite

class AlgorithmSuite extends AnyFunSuite {

  private val controller = controllerFor(new SliderBoard(3))

  test("the fast algorithms use the fast solver") {
    assert(Algorithm.FAST_SHORTEST.createSolver(controller).isInstanceOf[FastSliderSolver])
    assert(Algorithm.FAST_SHORTEST_IF_QUICK.createSolver(controller).isInstanceOf[FastSliderSolver])
  }

  test("every algorithm can make a solver") {
    Algorithm.values.foreach(a => assert(a.createSolver(controller) != null))
  }
}
