// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.reduction

import com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch.GridGeometry
import org.scalatest.funsuite.AnyFunSuite

class StepSearchSuite extends AnyFunSuite {

  private val geometry = new GridGeometry(3)
  private val nothingFixed = new Array[Boolean](geometry.numCells)

  test("a tile that is already home needs no slides") {
    assertResult(0)(new StepSearch(geometry, nothingFixed).shortestSlides(8, Array(0), Array(0)).get.length)
  }

  test("a tile next to home with the blank on it needs one slide") {
    assertResult(Seq(1))(new StepSearch(geometry, nothingFixed).shortestSlides(0, Array(1), Array(0)).get.toSeq)
  }

  test("the blank has to get to the far side of the tile before it can pull it home") {
    val slides = new StepSearch(geometry, nothingFixed).shortestSlides(4, Array(1), Array(2)).get
    assertResult(3)(slides.length)
  }

  test("does not slide through cells that are fixed") {
    val fixed = Array(true, false, false, false, false, false, false, false, false)
    val slides = new StepSearch(geometry, fixed).shortestSlides(4, Array(3), Array(1)).get
    assert(!slides.contains(0))
  }

  test("finds nothing when fixed cells wall the tile in") {
    val fixed = Array(false, true, false, true, false, false, false, false, false)
    assert(new StepSearch(geometry, fixed).shortestSlides(4, Array(0), Array(8)).isEmpty)
  }

  test("brings two tiles home together") {
    val slides = new StepSearch(geometry, nothingFixed).shortestSlides(8, Array(3, 5), Array(1, 2)).get
    assert(slides.nonEmpty)
  }
}
