// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import com.barrybecker4.puzzle.sudoku.model.ValueConverter

/**
  * The numbers which might be the valid value for a specific cell.
  * In java, I used ConcurrentSkipListSet, but now replacing that with immutable Set
  *
  * @author Barry Becker
  */
class Candidates(list: ValuesList) {

  var elements: Set[Int] = Set()

  list.elements.foreach(x => elements += x)

  def this() {
    this(new ValuesList(0))
  }

  def this(cands: Candidates) {
    this(new ValuesList(cands.elements))
  }

  def this(cands: Int*) {
    this(new ValuesList(cands))
  }

  def copy = new Candidates(this)

  def getFirst: Integer = elements.iterator.next

  def add(v: Int): Unit = elements += v
  def addAll(values: Candidates): Unit = addAll(values.elements)
  def addAll(values: ValuesList): Unit = addAll(values.elements)
  def addAll(seq: Iterable[Int]): Unit = seq.foreach(x => add(x))
  def retainAll(cands: Candidates): Unit = elements = elements.filter(cands.contains)
  def remove(v: Int): Unit = elements -= v
  def removeAll(cands: Candidates): Unit = {
    elements = elements.filter(x => !cands.contains(x))
  }
  def contains(v: Int): Boolean = elements.contains(v)
  def containsAll(cands: Candidates): Boolean = elements.forall(cands.contains)
  def clear(): Unit = { elements = Set() }
  def size: Int = elements.size

  override def toString: String = {
    val bldr = new StringBuilder("[")
    for (v <- elements) {
      bldr.append(ValueConverter.getSymbol(v)).append(",")
    }
    bldr.substring(0, bldr.length - 1) + "]"
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Candidates]


  override def equals(other: Any): Boolean = other match {
    case that: Candidates =>
      (that canEqual this) &&
        elements == that.elements
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(elements)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
