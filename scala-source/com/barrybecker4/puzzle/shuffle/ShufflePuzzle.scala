// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.shuffle

/**
  * Given a deck of nCards unique cards, cut the deck iCut cards from top and perform a perfect shuffleUntilSorted.
  * A perfect shuffleUntilSorted begins by putting down the bottom card from the top portion of the deck followed by the bottom card
  * from the bottom portion of the deck followed by the next card from the top portion, etc., alternating cards
  * until one portion is used up. The remaining cards go on top. The problem is to find the number of
  * perfect shuffles required to return the deck to its original order. Your function should be declared as:
  *
  * static long shuffleUntilSorted(int nCards, int iCut);
  *
  * Find the result for shuffles(402, 101)
  * Find the result for shuffles(802, 101)
  * Find the result for shuffles(1002, 101)
  *
  * @author Barry Becker Date: Jan 3, 2006
  */
object ShufflePuzzle {
  def main(args: Array[String]) {
    val nCards = 802
    // 402, 802, 1002
    val iCut = 101
    val puzzle = new ShufflePuzzle
    // Copare the two types of algorithm
    puzzle.shuffleUntilSorted(new Deck2(nCards), iCut) // analytic
    puzzle.shuffleUntilSorted(new Deck1(nCards), iCut) // brute force
  }
}

class ShufflePuzzle private() {
  private def shuffleUntilSorted(deck: Deck, iCut: Int) {
    val time = System.currentTimeMillis
    val numShuffles = deck.shuffleUntilSorted(iCut)
    System.out.println("A sorted deck of " + deck.size + " cards, cut at " + iCut + " deep takes " + numShuffles + " perfect shuffles to restore the deck.")
    System.out.println("time elapsed = " + (System.currentTimeMillis - time) + " milliseconds")
  }
}

