// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import scala.util.Random

/**
  * The list of values in a bigCell (or row or column).
  *
  * @author Barry Becker
  */
object ValuesList {

  def getShuffledCandidates(cands: Candidates): ValuesList = {
    if (cands == null) return new ValuesList(0)
    ValuesList.createShuffledList(cands)
  }

  private def createShuffledList(cands: Candidates) = {
    val list = new ValuesList(cands.elements)
    list.shuffle()
    list
  }
}

class ValuesList(els: Iterable[Int]) {

  var elements: List[Int] = List()

  addAll(els)

  def this(n: Int) = this(1 to n)
  def addAll(cands: Candidates): Unit = addAll(cands.elements)
  def addAll(els: Iterable[Int]): Unit = els.foreach(x => elements +:= x)

  def shuffle(): Unit = Random.shuffle(elements)
}
