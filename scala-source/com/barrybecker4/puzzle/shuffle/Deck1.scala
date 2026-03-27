// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle

/**
  * Brute-force iteration count for the perfect-shuffle problem (same permutation as [[Deck2]]).
  * @author Barry Becker
  */
class Deck1(val nCards: Int) extends Deck {

  if (nCards < 1)
    throw new IllegalArgumentException("Deck must contain at least one card. You entered " + nCards + '.')

  private var data: Array[Int] = Array.tabulate(nCards)(identity)

  def size: Int = data.length
  def get(i: Int): Int = data(i)

  def shuffleUntilSorted(iCut: Int): Long = {
    var ct = 1L
    doPerfectShuffle(iCut)
    while (!isSorted && ct < Int.MaxValue) {
      doPerfectShuffle(iCut)
      ct += 1
    }
    require(ct < Int.MaxValue, "No amount of shuffling will restore the order")
    ct
  }

  private def isSorted: Boolean =
    data.length <= 1 ||
      (1 until data.length).forall(i => data(i) == data(i - 1) + 1)

  def doPerfectShuffle(iCut: Int): Unit =
    PerfectShuffle.applyInPlace(data, iCut)

  override def toString: String = "Deck: " + data.mkString(", ")
}
