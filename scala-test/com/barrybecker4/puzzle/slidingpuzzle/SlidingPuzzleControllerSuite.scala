// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.testsupport.i18n.StubMessageContext
import com.barrybecker4.puzzle.slidingpuzzle.model.{ShuffleStyle, Solvability}
import org.scalatest.funsuite.AnyFunSuite

class SlidingPuzzleControllerSuite extends AnyFunSuite {

  AppContext.injectMessageContext(new StubMessageContext)

  test("the puzzle starts as a solvable 8 puzzle") {
    val board = new SlidingPuzzleController(null).initialState
    assertResult(3)(board.size)
    assert(Solvability.isSolvable(3, board.tiles.tiles.map(_.toInt)))
  }

  test("setting the size and style makes a solvable puzzle of that size") {
    val controller = new SlidingPuzzleController(null)
    for (style <- ShuffleStyle.values; size <- 2 to 5) {
      controller.setSize(size, style)
      assertResult(size)(controller.initialState.size)
      assert(Solvability.isSolvable(size, controller.initialState.tiles.tiles.map(_.toInt)))
    }
  }

  test("the default style is the easy random walk") {
    val controller = new SlidingPuzzleController(null)
    controller.setSize(5)
    assert(controller.initialState.distanceToGoal < 60)
  }
}
