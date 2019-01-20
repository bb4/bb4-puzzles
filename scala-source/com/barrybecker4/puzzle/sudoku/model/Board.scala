// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import com.barrybecker4.puzzle.sudoku.model.BoardComponents.COMPONENTS

class Cell(var originalValue: Int, var proposedValue: Int)

/**
  * The Board describes the physical layout of the puzzle.
  * The number of Cells in the board is n^2 * n^2, but there are n * n big cells.
  * This implementation is based on Peter Norvig's algorithm - http://norvig.com/sudoku.html
  * @param initialData array giving initially set values
  * @author Barry Becker
  */
class Board(val initialData: Array[Array[Cell]]) {

  type ValueMap = Map[(Int, Int), Set[Int]]
  val edgeLength: Int = initialData.length
  val baseSize: Int = Math.sqrt(edgeLength).toInt
  private val comps = COMPONENTS(baseSize)
  private var valuesMap: ValueMap = _
  val numCells: Int = edgeLength * edgeLength
  var numIterations = 0
  reset()

  def this(initial: Array[Array[Int]]) = this(initial.map(_.map(v => new Cell(v, v))))
  def this(baseSize: Int) = this(Array.ofDim[Int](baseSize * baseSize, baseSize * baseSize))
  def copy() = new Board(initialDataCopy)

  private def initialDataCopy = initialData.map(_.map(c => new Cell(c.originalValue, c.proposedValue)))

  def getCell(location: (Int, Int)): Cell = initialData(location._1 - 1)(location._2 - 1)

  def reset() {
    valuesMap = (for (s <- comps.squares) yield s -> comps.digits.toSet).toMap
    numIterations = 0
  }

  /** @return true if the board has been successfully solved. Solved if all candidates a single value. */
  def isSolved: Boolean = valuesMap.values.forall(_.size == 1)
  def getValues(location: (Int, Int)): Seq[Int] = valuesMap(location).toList

  def getValue(location: (Int, Int)): Int = {
    valuesMap(location) match {
      case singleValue if singleValue.size == 1 => singleValue.head
      case notSingle => 0
    }
  }

  /** Sets the original value, and update valuesMap accordingly */
  def setOriginalValue(location: (Int, Int), v: Int): Unit =
    initialData(location._1 - 1)(location._2 - 1) = new Cell(v, v)

  /** Remove the specified value if it does not prevent the puzzle from being solved using just the
    * basic consistency check.
    */
  def removeValueIfPossible(location: (Int, Int), refresh: Option[() => Unit] = None) {
    val initial = initialDataCopy
    initial(location._1 - 1)(location._2 - 1) = new Cell(0, 0)

    val b = new Board(initial)
    b.updateFromInitialData()
    if (b.isSolved) {
      initialData(location._1 - 1)(location._2 - 1) = new Cell(0, 0)
      this.valuesMap = b.valuesMap
      if (refresh.isDefined) refresh.get()
    }
  }

  /** return true if solved, else false */
  def solve(refresh: Option[() => Unit] = None): Boolean = {
    if (updateFromInitialData()) {
      searchForSolution(Some(valuesMap), refresh) match {
        case Some(vals) =>
          valuesMap = vals
          return true
        case None => return false
      }
    }
    false
  }

  def setSolvedValues(): Unit = {
    for ((s, values) <- valuesMap) {
      if (values.size == 1) initialData(s._1 - 1)(s._2 - 1).proposedValue = values.head
    }
  }

  private def searchForSolution(values: Option[ValueMap], refresh: Option[() => Unit]): Option[ValueMap] = {
    values match {
      case None => None
      case Some(vals) =>
        if (vals.values.forall(_.size == 1)) return Some(vals)
        // Chose the unfilled square s with the fewest possibilities
        val minSq: (Int, Int) = (for (s <- comps.squares; if vals(s).size > 1)
          yield (vals(s).size, s)).min._2
        for (d <- vals(minSq)) {
          numIterations += 1
          doRefresh(refresh)
          val result = searchForSolution(assign(vals, minSq, d), refresh)
          if (result.nonEmpty) return result
        }
        None
    }
  }

  /** @return true if updated successfully. False if there was an inconsistency. */
  def updateFromInitialData(): Boolean = {
    for (r <- comps.digits; c <- comps.digits; v = initialData(r - 1)(c - 1).originalValue; if v > 0)
      assign(valuesMap, (r, c), v) match {
        case Some(values) => valuesMap = values
        case None => return false
      }
    true
  }

  private def doRefresh(refresh: Option[() => Unit]) {
    if (refresh.isDefined) {
      setSolvedValues()
      refresh.get()
    }
  }

  /**
    * Assign a value to a square if possible.
    * Eliminate all the other values (except d) from values[s] and propagate.
    * @return Some(values), except return None if a contradiction is detected.
    */
  private def assign(values: ValueMap, s: (Int, Int), d: Int): Option[ValueMap] = {
    val otherValues: Set[Int] = values(s) - d
    var newValues: Option[ValueMap] = Some(values)
    for (d2 <- otherValues) {
      newValues = eliminate(newValues.get, s, d2)
      if (newValues.isEmpty) return None
    }
    newValues
  }

  /**
    * Eliminate d from values[s]; propagate when values or places <= 2.
    * @return Some(values), except return None if a contradiction is detected.
    */
  private def eliminate(values: ValueMap, s: (Int, Int), d: Int): Option[ValueMap] = {

    if (!values(s).contains(d)) return Some(values)
    var newValues = values.updated(s, values(s) - d)
    // If a square s is reduced to one value, d2, then eliminate d2 from the peers.
    if (newValues(s).isEmpty)
      return None // Contradiction
    // val candidates = newValues(s)
    else if (newValues(s).size == 1) {
      val d2 = newValues(s).head
      for (s2 <- comps.peers(s)) {
        eliminate(newValues, s2, d2) match {   // Recursive call
          case Some(vals) => newValues = vals
          case None => return None // Contradiction
        }
      }
    }
    // If a unit u is reduced to only one place for a value d, then put it there.
    for (u <- comps.units(s)) {
      val dplaces = for (s <- u; if newValues(s).contains(d)) yield s
      if (dplaces.isEmpty) return None // Contradiction
      if (dplaces.size == 1) {
        // d can only be in one place in unit; assign it there
        assign(newValues, dplaces.head, d) match {
          case Some(vals) => newValues = vals
          case None => return None  // Contradiction
        }
      }
    }
    Some(newValues)
  }

  override def toString: String = {
    val b = for (r <- comps.digits) yield
      for (c <- comps.digits; v = initialData(r-1)(c-1)) yield v.proposedValue
    "\n" + b.map(_.map(ValueConverter.getSymbol).mkString("Array(", ", ", "),")).mkString("\n")
  }

  def toDebugString: String = {
    val b = for (r <- comps.digits) yield
      for (c <- comps.digits; v = valuesMap((r, c))) yield v
    b.map(_.map(_.map(ValueConverter.getSymbol)).mkString("[", ",", "]")).mkString("\n")
  }
}
