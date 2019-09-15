// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle

/**
  * @author Barry Becker
  */
class Deck1 (val nCards: Int) extends Deck {

  private var data:Array[Int] = new Array[Int](nCards)
  for (i <- 0 until nCards) data(i) = i

  def size: Int = data.length
  def get(i: Int): Int = data(i)

  def shuffleUntilSorted(iCut: Int): Long = {
    assert(iCut < size)
    var ct = 1
    doPerfectShuffle(iCut)
    while (!isSorted && ct < Integer.MAX_VALUE) {
      doPerfectShuffle(iCut)
      ct += 1
      if (ct % 100000 == 0) println(ct + "  " + this)
    }
    assert(ct < Integer.MAX_VALUE, "No amount of shuffling will restore the order")
    ct
  }

  private def isSorted: Boolean  = {
    if (data == null || data.length == 1) true
    else {
      for (i <- 1 until data.length) {
        if (data(i) != data(i - 1) + 1) return false
      }
      true
    }
  }

  def doPerfectShuffle(iCut: Int) {
    val size = data.length
    val temp = new Array[Int](size)
    val loop = Math.min(iCut, size - iCut)
    var ct = size - 1

    for (i <- 0 until loop) {
      temp(ct) = data(iCut - 1 - i)
      ct -= 1
      temp(ct) = data(size - 1 - i)
      ct -= 1
    }
    if (iCut < size - iCut) {
      for (i <- size - iCut - 1 to iCut by -1) {
        temp(ct) = data(i)
        ct -= 1
      }
    }
    else {
      for (i <- iCut - 1 to  size - iCut by -1) {
        temp(ct) = data(i)
        ct -= 1
      }
    }
    assert(ct == -1, "ct = " + ct)
    data = temp // replace our data with the result of the shuffleUntilSorted
  }

  override def toString: String = {
    val buf = new StringBuilder("Deck: ")
    for (i <- 0 until data.length - 1) {
        buf.append(data(i)).append(", ")
    }
    buf.append(data(data.length - 1))
    buf.toString
  }
}
