package com.barrybecker4.puzzle.shuffle

/**
  * @author Barry Becker
  */
trait Deck {

  def size: Int

  def get(i: Int): Int

  def shuffleUntilSorted(iCut: Int): Long

  def doPerfectShuffle(iCut: Int)
}
