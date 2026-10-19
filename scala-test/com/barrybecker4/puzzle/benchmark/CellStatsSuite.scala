// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.benchmark

import org.scalatest.funsuite.AnyFunSuite

class CellStatsSuite extends AnyFunSuite {

  test("median of nothing is nothing") {
    assert(CellStats.median(Nil).isEmpty)
  }

  test("median of an odd number of values is the middle one") {
    assert(CellStats.median(List(30L, 10L, 20L)).contains(20L))
  }

  test("median of an even number of values is the average of the middle two") {
    assert(CellStats.median(List(40L, 10L, 20L, 30L)).contains(25L))
  }

  test("only solved runs contribute to the timings") {
    val cell = CellStats.aggregate("Tantrix", "6 tiles", "GENETIC_SEARCH", List(
      Solved(300, 30), Solved(100, 10), Unsolved(50, 5), TimedOut(7), Errored("oops")
    ))
    assert(cell.attempts == 5)
    assert(cell.solved == 2)
    assert(cell.unsolved == 1)
    assert(cell.timedOut == 1)
    assert(cell.errored == 1)
    assert(cell.medianMillis.contains(200L))
    assert(cell.minMillis.contains(100L))
    assert(cell.maxMillis.contains(300L))
    assert(cell.medianTries.contains(20L))
    assert(cell.wasSolved)
  }

  test("the cell remembers the most recent error") {
    val cell = CellStats.aggregate("Red Puzzle", "9 pieces", "GENETIC_SEARCH", List(Errored("first"), Errored("second")))
    assert(cell.errored == 2)
    assert(cell.lastError.contains("second"))
    assert(CellStats.aggregate("P", "v", "A", List(Solved(1, 1))).lastError.isEmpty)
  }

  test("a cell with no solved runs has no timings") {
    val cell = CellStats.aggregate("Tantrix", "15 tiles", "GENETIC_SEARCH", List(TimedOut(1), TimedOut(2)))
    assert(!cell.wasSolved)
    assert(cell.medianMillis.isEmpty)
    assert(cell.minMillis.isEmpty)
    assert(cell.maxMillis.isEmpty)
    assert(cell.medianTries.isEmpty)
  }
}
