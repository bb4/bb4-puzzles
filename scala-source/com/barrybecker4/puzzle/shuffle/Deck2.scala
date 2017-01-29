package com.barrybecker4.puzzle.shuffle

import com.barrybecker4.common.math.MathUtil

/**
  * Analytic solution to the card shuffling problem.
  * Web references:
  * http://www.math.ucsd.edu/~ronspubs/83_05_shuffles.pdf
  * http://www.oreillynet.com/pub/wlg/5094 (Scott's original link, now broken)
  * https://www.math.hmc.edu/funfacts/ffiles/20001.1-6.shtml
  * https://coderanch.com/t/35113/Card-Shuffle
  *
  * @author Scott Sauyet
  */
object Deck2 {
  def main(args: Array[String]) {
    assert(args.length == 2, "Usage: java Deck nCards iCut")
    var cards = 1002
    var cut = 101
    try {
      cards = args(0).toInt
      cut = args(1).toInt
    } catch {
      case nfe: NumberFormatException =>
        System.out.println("Arguments must be numeric.")
        System.exit(2)
    }
    val start = System.currentTimeMillis
    val deck = new Deck2(cards)
    val result = deck.shuffleUntilSorted(cut)
    val time = System.currentTimeMillis - start
    System.out.println("A perfect shuffleUntilSorted on " + cards + " cards, cut " + cut + " deep, takes " + result + " iterations to restore" + " the deck.")
    System.out.println("Calculation performed in " + time + "ms.")
  }
}

class Deck2(var count: Int) extends Deck {
  if (count < 1) throw new IllegalArgumentException("Deck must contain at least " + "one card.  You entered " + count + '.')
  private var cards = new Array[Int](count)
  for (i <- 0 until count) cards(i) = i

  def shuffleUntilSorted(iCut: Int): Long = {
    this.doPerfectShuffle(iCut)
    MathUtil.lcm(getCycles)
  }

  def size: Int = cards.length

  def get(i: Int): Int = cards(i)

  def doPerfectShuffle(theCut: Int) {
    val cut = theCut % count
    val top = new Array[Int](cut)
    System.arraycopy(cards, 0, top, 0, cut)
    val bottom = new Array[Int](count - cut)
    System.arraycopy(cards, cut, bottom, 0, count - cut)
    val updated = new Array[Int](count)
    val shared = Math.min(cut, count - cut)
    val different = count - shared - shared
    val extras = new Array[Int](different)
    if (cut > count - cut) System.arraycopy(top, 0, extras, 0, different)
    else System.arraycopy(bottom, 0, extras, 0, different)

    for (i <- 0 until shared) {
      updated(count - (2 * i) - 1) = top(cut - i - 1)
      updated(count - (2 * i) - 2) = bottom(count - cut - i - 1)
    }
    System.arraycopy(extras, 0, updated, 0, different)
    cards = updated
  }

  private def getCycles = {
    val cycles = new Array[Int](count)
    for (i <- 0 until count) {
      var index = i
      var result = 1
      while (cards(index) != i) {
        result += 1
        index = cards(index)
      }
      cycles(i) = result
    }
    cycles
  }

  override def toString: String = {
    val buffer = new StringBuilder
    buffer.append("Deck [")
    buffer.append(cards(0))

    for (i <- 1 until count) {
        buffer.append(", ")
        buffer.append(cards(i))
    }
    buffer.append("]")
    buffer.toString
  }
}

