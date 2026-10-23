// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

import com.barrybecker4.puzzle.slidingpuzzle.solver.SliderTestSupport.randomSolvableTiles
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class SliderStateSuite extends AnyFunSuite {

  private val geometry = new GridGeometry(4)
  private val solved = (1 to 15) :+ 0

  /** What the estimate should be, worked out from scratch. */
  private def freshEstimate(state: SliderState): Int =
    new SliderState(geometry, (0 until geometry.numCells).map(state.tileAt)).estimate

  test("a solved board has an estimate of 0") {
    assertResult(0)(new SliderState(geometry, solved).estimate)
  }

  test("one slide from solved has an estimate of 1") {
    val state = new SliderState(geometry, solved)
    state.slideIntoBlank(14)
    assertResult(1)(state.estimate)
    assertResult(14)(state.blankCell)
  }

  test("the running estimate always matches the estimate worked out from scratch") {
    val rnd = new Random(7)
    val state = new SliderState(geometry, randomSolvableTiles(4, rnd))
    for (_ <- 1 to 2000) {
      state.slideIntoBlank(geometry.neighbors(state.blankCell)(rnd.nextInt(geometry.neighbors(state.blankCell).length)))
      assertResult(freshEstimate(state))(state.estimate)
    }
  }

  test("sliding a tile and then sliding it back restores the board and the estimate") {
    val state = new SliderState(geometry, randomSolvableTiles(4, new Random(3)))
    val before = (state.estimate, state.blankCell)
    val cell = geometry.neighbors(state.blankCell).head
    val blank = state.blankCell
    state.slideIntoBlank(cell)
    state.slideIntoBlank(blank)
    assertResult(before)((state.estimate, state.blankCell))
  }

  test("the estimate is never more than the slides it took to make the board") {
    val rnd = new Random(11)
    val state = new SliderState(geometry, solved)
    for (slides <- 1 to 60) {
      val options = geometry.neighbors(state.blankCell)
      state.slideIntoBlank(options(rnd.nextInt(options.length)))
      assert(state.estimate <= slides)
    }
  }
}
