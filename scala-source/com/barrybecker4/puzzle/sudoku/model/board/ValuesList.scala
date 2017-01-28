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
    if (cands == null) return new ValuesList()
    ValuesList.createShuffledList(cands)
  }

  private def createShuffledList(cands: Candidates) = {
    val list = new ValuesList(cands)
    list.shuffle()
    list
  }
}

class ValuesList(candidates: Candidates) {

  var elements: List[Int] = List()

  def this() = this(new Candidates())

  def addAll(cands: Candidates): Unit = cands.elements.foreach(x => elements +:= x)

  def shuffle(): Unit = Random.shuffle(elements)
}
