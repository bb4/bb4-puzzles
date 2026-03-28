// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Action.*
import com.barrybecker4.puzzle.twopails.model.PourOperation.Container.*
import org.scalatest.funsuite.AnyFunSuite

class PailsSuite extends AnyFunSuite {

  private val example = PailParams(9, 4, 6)

  test("FILL tops the chosen pail") {
    val p = Pails(0, 0, example)
    assert(p.applyMove(PourOperation(FILL, FIRST), undo = false) == Pails(9, 0, example))
    assert(p.applyMove(PourOperation(FILL, SECOND), undo = false) == Pails(0, 4, example))
  }

  test("EMPTY clears the chosen pail") {
    val p = Pails(5, 3, example)
    assert(p.applyMove(PourOperation(EMPTY, FIRST), undo = false) == Pails(0, 3, example))
    assert(p.applyMove(PourOperation(EMPTY, SECOND), undo = false) == Pails(5, 0, example))
  }

  test("TRANSFER first to second pours min of source and free space in dest") {
    val p = Pails(9, 0, example)
    assert(p.applyMove(PourOperation(TRANSFER, FIRST), undo = false) == Pails(5, 4, example))
  }

  test("TRANSFER second to first") {
    val p = Pails(2, 4, example)
    assert(p.applyMove(PourOperation(TRANSFER, SECOND), undo = false) == Pails(6, 0, example))
  }

  test("TRANSFER partial when destination has liquid") {
    val p = Pails(7, 2, PailParams(7, 4, 3))
    assert(p.applyMove(PourOperation(TRANSFER, FIRST), undo = false) == Pails(5, 4, PailParams(7, 4, 3)))
  }

  test("isSolved when either side matches target") {
    assert(Pails(6, 0, example).isSolved)
    assert(Pails(0, 6, example).isSolved)
    assert(!Pails(5, 0, example).isSolved)
  }

  test("undo via reverse matches round trip for TRANSFER") {
    val op = PourOperation(TRANSFER, FIRST)
    val before = Pails(9, 0, example)
    val after = before.applyMove(op, undo = false)
    assert(before == after.applyMove(op, undo = true))
  }
}
