// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle

import org.scalatest.funsuite.AnyFunSuite

class DeckSuite extends AnyFunSuite {

  test("Deck1 and Deck2 produce identical permutation after one perfect shuffle") {
    for (n <- 3 to 24; c <- 0 until n) {
      val d1 = new Deck1(n)
      val d2 = new Deck2(n)
      d1.doPerfectShuffle(c)
      d2.doPerfectShuffle(c)
      for (i <- 0 until n) {
        assert(d1.get(i) === d2.get(i), s"n=$n cut=$c position=$i")
      }
    }
  }

  test("shuffleUntilSorted: Deck1 matches Deck2 for small decks") {
    val cases = Seq(
      (6, 2),
      (8, 3),
      (10, 4),
      (12, 5),
      (14, 3),
      (16, 7)
    )
    for ((n, cut) <- cases) {
      val brute = new Deck1(n).shuffleUntilSorted(cut)
      val analytic = new Deck2(n).shuffleUntilSorted(cut)
      assert(brute === analytic, s"n=$n cut=$cut brute=$brute analytic=$analytic")
    }
  }

  /** Values from Deck2.shuffleUntilSorted (analytic); used as regression checks for the puzzle statement. */
  test("Deck2 analytic result matches known puzzle values") {
    assert(new Deck2(402).shuffleUntilSorted(101) === 13248L)
    assert(new Deck2(802).shuffleUntilSorted(101) === 37758L)
    assert(new Deck2(1002).shuffleUntilSorted(101) === 5812104600L)
  }

  test("Deck1 and Deck2 reject empty deck") {
    intercept[IllegalArgumentException](new Deck1(0))
    intercept[IllegalArgumentException](new Deck2(0))
  }
}
