// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import java.awt.Container

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.sudoku.model.ValueConverter
import com.barrybecker4.puzzle.sudoku.model.board.BoardComponents.COMPONENTS

/**
  * The Board describes the physical layout of the puzzle.
  * The number of Cells in the board is n^2 * n^2, but there are n * n big cells.
  * This implementation is based on Peter Norvig's algorithm described at http://norvig.com/sudoku.html
  *
  * @param initialData array giving initially set values
  * @author Barry Becker
  */
class Board(initialData: Array[Array[Int]]) {

  val edgeLength: Int = initialData.length
  val baseSize: Int = Math.sqrt(edgeLength).toInt
  val numCells: Int = edgeLength * edgeLength
  var numIterations = 0

  private val comps = COMPONENTS(baseSize)

  private var valuesMap: Map[(Int, Int), Set[Int]] = (for (s <- comps.squares) yield s -> comps.digits.toSet).toMap
  updateFromInitialData()

  def this(size: Int) = this(Array.ofDim[Int](size, size))

  def isOriginal(location: Location): Boolean = initialData(location.getRow)(location.getCol) > 0

  def getValue(location: Location): Int = {
    valuesMap((location.getRow, location.getCol)) match {
      case singleValue if singleValue.size == 1 => singleValue.head
      case notSingle => 0
    }
  }

  def getValues(location: Location): Seq[Int] = valuesMap((location.getRow, location.getCol)).toList

  def setOriginalValue(location: Location, v: Int) {
    initialData(location.getRow)(location.getCol) = v
    assign(valuesMap, (location.getRow, location.getCol), v) match {
      case Some(vals) => valuesMap = vals
      case None => throw new IllegalStateException("Cannot set a value there because it would be inconsistent!")
    }
  }

  def clearValueIfPossible(location: (Int, Int)): Board = {
    val initial = initialData.clone()
    initial(location._1)(location._2) = 0

    var testBoard: Board = this
    try {
      val b = new Board(initial)
      testBoard = if (b.solve()) b else this
    } catch {
      case e: IllegalStateException => testBoard = this
    }
    testBoard
  }

  /** return true if solved, else false */
  def solve(panel: Container = null): Boolean = {
    searchForSolution(Some(valuesMap), panel) match {
      case Some(vals) =>
        valuesMap = vals
        true
      case None => false
    }
  }

  private def searchForSolution(values: Option[Map[(Int, Int), Set[Int]]],
                                panel: Container = null): Option[Map[(Int, Int), Set[Int]]] = {
    values match {
      case None => None
      case Some(vals) =>
        if (comps.squares.forall(vals(_).size == 1)) return Some(vals)
        // Chose the unfilled square s with the fewest possibilities
        val minSq: (Int, Int) = (for (s <- comps.squares; if vals(s).size > 1) yield (vals(s).size, s)).min._2
        for (d <- vals(minSq)) {
          numIterations += 1
          val result = searchForSolution(assign(vals, minSq, d))
          if (result.nonEmpty) {
            return result
          }
        }
        None
    }
  }

  private def updateFromInitialData() = {
    for (r <- comps.digits; c <- comps.digits; v = initialData(r)(c)) {
      if (v > 0) {
        assign(valuesMap, (r, c), v) match {
          case Some(values) => valuesMap = values
          case None => throw new IllegalStateException("Not a valid initial board state")
        }
      }
    }
  }

  /**
    * Assign a value to a squre if possible. Eliminate all the other values (except d) from values[s] and propagate.
    * @return Some(values), except return None if a contradiction is detected.
    */
  private def assign(values: Map[(Int, Int), Set[Int]], s: (Int, Int), d: Int): Option[Map[(Int, Int), Set[Int]]] = {

    val otherValues: Set[Int] = values(s) - d

    var newValues: Option[Map[(Int, Int), Set[Int]]] = Some(values)
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
  private def eliminate(values: Map[(Int, Int), Set[Int]], s: (Int, Int), d: Int): Option[Map[(Int, Int), Set[Int]]] = {

    if (!values(s).contains(d)) return Some(values)
    var newValues = values.updated(s, values(s) - d)
    // If a square s is reduced to one value, d2, then eliminate d2 from the peers.
    if (newValues(s).isEmpty) return None // Contradiction
    else if (newValues(s).size == 1) {
      val d2 = newValues(s).head
      for (s2 <- comps.peers(s)) {
        val vals = eliminate(newValues, s2, d2)
        if (vals.isEmpty) return None // Contradiction
        else newValues = vals.get
      }
    }
    // If a unit u is reduced to only one place for a value d, then put it there.
    for (u <- comps.units(s)) {
      val dplaces = for (d <- newValues(s); if newValues(s).contains(d)) yield s
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

  /** @return true if the board has been successfully solved. */
  def solved: Boolean = false // FIX



  override def toString: String = {

    val b = for (r <- comps.digits) yield
      for (c <- comps.digits; v = valuesMap((r, c))) yield v

    b.map(_.map(_.map(ValueConverter.getSymbol)).mkString("[", ",", "]")).mkString("\n")
  }
}
