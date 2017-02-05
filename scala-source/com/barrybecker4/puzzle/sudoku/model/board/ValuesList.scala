// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.puzzle.sudoku.model.board.ValuesList.RAND

import scala.util.Random

/**
  * The list of values in a bigCell (or row or column).
  *
  * @author Barry Becker
  */
object ValuesList {

  private val RAND = new Random(Math.round(Math.random() * 100000))

  def getShuffledCandidates(cands: Candidates, rand: Random = RAND): ValuesList = {
    if (cands.isEmpty) return new ValuesList(0)
    ValuesList.createShuffledList(cands, rand)
  }

  private def createShuffledList(cands: Candidates, rand: Random = RAND) = {
    val list = new ValuesList(cands.elements)
    list.shuffle(rand)
    list
  }
}

class ValuesList(els: Iterable[Int]) {

  var elements: List[Int] = List()

  addAll(els)

  def this(n: Int) = this(1 to n)
  def addAll(cands: Candidates): Unit = addAll(cands.elements)
  def addAll(els: Iterable[Int]): Unit = els.foreach(x => elements +:= x)

  def shuffle(rand: Random = RAND): Unit =
    elements = rand.shuffle(elements)

  def canEqual(other: Any): Boolean = other.isInstanceOf[ValuesList]

  override def equals(other: Any): Boolean = other match {
    case that: ValuesList =>
      (that canEqual this) &&
        elements == that.elements
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(elements)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString: String = elements.mkString(",")
}
