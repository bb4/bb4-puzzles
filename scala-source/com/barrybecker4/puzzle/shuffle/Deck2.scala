// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle

import com.barrybecker4.math.MathUtil

/**
  * Analytic solution to the card shuffling problem (order of the permutation = lcm of cycle lengths).
  * Web references:
  * http://www.math.ucsd.edu/~ronspubs/83_05_shuffles.pdf
  * http://www.oreillynet.com/pub/wlg/5094 (Scott's original link, now broken)
  * https://www.math.hmc.edu/funfacts/ffiles/20001.1-6.shtml
  * https://coderanch.com/t/35113/Card-Shuffle
  * @author Scott Sauyet
  */
object Deck2 {

  def main(args: Array[String]): Unit = {
    val (cards, cut) = args.length match {
      case 0 =>
        // Defaults when run from Gradle/IDE without program arguments (matches original demo).
        (1002, 101)
      case 2 =>
        try (args(0).toInt, args(1).toInt)
        catch {
          case _: NumberFormatException =>
            println("Arguments must be numeric.")
            sys.exit(2)
        }
      case _ =>
        println("Usage: Deck2 [nCards iCut]")
        println("  With no arguments, uses 1002 cards and cut 101.")
        sys.exit(1)
    }
    val start = System.currentTimeMillis()
    val deck = new Deck2(cards)
    val result = deck.shuffleUntilSorted(cut)
    val time = System.currentTimeMillis() - start
    println(
      "A perfect shuffle on " + cards + " cards, cut " + cut
        + " deep, takes " + result + " iterations to restore the deck."
    )
    println("Calculation performed in " + time + "ms.")
  }
}

class Deck2(val count: Int) extends Deck {
  if (count < 1)
    throw new IllegalArgumentException("Deck must contain at least one card. You entered " + count + '.')

  private var cards: Array[Int] = Array.tabulate(count)(identity)

  def shuffleUntilSorted(iCut: Int): Long = {
    doPerfectShuffle(iCut)
    MathUtil.lcm(cycleLengthsForPermutation)
  }

  def size: Int = cards.length

  def get(i: Int): Int = cards(i)

  def doPerfectShuffle(theCut: Int): Unit =
    PerfectShuffle.applyInPlace(cards, theCut)

  /** Length of the cycle containing `i` under `j -> cards(j)` (matches the original counting scheme). */
  private def cycleLengthFrom(i: Int): Int = {
    var index = i
    var result = 1
    while (cards(index) != i) {
      result += 1
      index = cards(index)
    }
    result
  }

  private def cycleLengthsForPermutation: Array[Int] = {
    val cycles = new Array[Int](count)
    for (i <- 0 until count) cycles(i) = cycleLengthFrom(i)
    cycles
  }

  override def toString: String = "Deck [" + cards.mkString(", ") + "]"
}
