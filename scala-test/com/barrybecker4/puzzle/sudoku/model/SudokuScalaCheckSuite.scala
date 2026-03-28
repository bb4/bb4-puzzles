// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import com.barrybecker4.puzzle.sudoku.data.TestData
import org.junit.Assert.assertTrue
import org.scalacheck.{Gen, Prop, Test as ScalaCheckTest}
import org.scalatest.funsuite.AnyFunSuite

/**
  * Property checks on solved boards (small, deterministic generators).
  */
class SudokuScalaCheckSuite extends AnyFunSuite {

  /** Solved puzzles from our test corpus. */
  private val basePuzzle: Gen[Array[Array[Int]]] = Gen.oneOf(
    TestData.SIMPLE_4,
    TestData.SIMPLE_9,
    TestData.DIFFICULT_9
  )

  test("ScalaCheck: each solved cell has a single candidate") {
    val p = Prop.forAll(basePuzzle) { initial =>
      val b = new Board(initial).solve()
      b.forall(_.valuesMap.values.forall(_.size == 1))
    }
    assertTrue(ScalaCheckTest.check(ScalaCheckTest.Parameters.default, p).passed)
  }

  test("ScalaCheck: each row of solved board is a permutation of 1..n") {
    val p = Prop.forAll(basePuzzle) { initial =>
      val b0 = new Board(initial).solve()
      b0.exists { b =>
        val n = b.edgeLength
        val digits = 1 to n
        (1 to n).forall { r =>
          val rowVals = (1 to n).map(c => b.getValue((r, c))).sorted
          rowVals == digits.toSeq
        }
      }
    }
    assertTrue(ScalaCheckTest.check(ScalaCheckTest.Parameters.default, p).passed)
  }
}
