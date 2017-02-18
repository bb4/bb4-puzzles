// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.puzzle.sudoku.model.board.ValuesList.RAND

import scala.util.Random

/**
  * The list of values in a bigCell, or a row or column).
  *
  * @author Barry Becker
  */
object ValuesList {

  private val RAND = new Random((Math.random() * 1000000).toInt)

  def getShuffledCandidates(cands: Candidates, rand: Random = RAND): ValuesList = {
    //if (cands.isEmpty) return new ValuesList(0)
    new ValuesList(rand.shuffle(cands.elements))
  }
}

class ValuesList(els: Iterable[Int]) {

  var elements: List[Int] = els.toList

  def this(n: Int) = this(1 to n)
  def addAll(cands: Candidates): Unit = addAll(cands.elements)
  def addAll(els: Iterable[Int]): Unit = elements = els.toList

  def shuffle(rand: Random = RAND): Unit =
    elements = rand.shuffle(elements)

  override def equals(other: Any): Boolean = other match {
    case that: ValuesList => elements == that.elements
    case _ => false
  }

  override def hashCode(): Int = elements.hashCode()
  override def toString: String = elements.mkString(",")
}
