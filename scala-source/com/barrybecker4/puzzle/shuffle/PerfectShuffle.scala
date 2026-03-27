// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle

/** In-place perfect shuffle used by [[Deck1]] and [[Deck2]]. */
private[shuffle] object PerfectShuffle {

  def applyInPlace(cards: Array[Int], iCut: Int): Unit = {
    val count = cards.length
    val cut = math.floorMod(iCut, count)
    val top = new Array[Int](cut)
    System.arraycopy(cards, 0, top, 0, cut)
    val bottom = new Array[Int](count - cut)
    System.arraycopy(cards, cut, bottom, 0, count - cut)
    val updated = new Array[Int](count)
    val shared = math.min(cut, count - cut)
    val different = count - shared - shared
    val extras = new Array[Int](different)
    if (cut > count - cut) System.arraycopy(top, 0, extras, 0, different)
    else System.arraycopy(bottom, 0, extras, 0, different)

    for (i <- 0 until shared) {
      updated(count - (2 * i) - 1) = top(cut - i - 1)
      updated(count - (2 * i) - 2) = bottom(count - cut - i - 1)
    }
    System.arraycopy(extras, 0, updated, 0, different)
    System.arraycopy(updated, 0, cards, 0, count)
  }
}
