// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import scala.util.Random
//import ValuesList.RAND

/**
  * The list of values in a bigCell (or row or column).
  *
  * @author Barry Becker
  */
object ValuesList {

  //private val RAND = new Random(Math.round(Math.random() * 100000))

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


  def shuffle(): Unit = {
    //val r = new Random(Math.round(Math.random() * 100000))
    //println("before shuffle = " + elements.mkString(", "))
    elements = Random.shuffle(elements)
  }

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
}
