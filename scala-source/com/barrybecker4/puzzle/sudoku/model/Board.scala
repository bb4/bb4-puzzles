// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import BoardComponents.COMPONENTS

import scala.collection.immutable.HashMap

object Board {
  private def arrayToMap(initial: Array[Array[Int]]) = {
    val baseSize = Math.sqrt(initial.length).toInt
    val allCandidates = COMPONENTS(baseSize).digits.toSet
    var map = new HashMap[Location, Cell]()
    for (i <- initial.indices) {
      val row = initial(i)
      for (j <- row.indices) {
        val v = initial(i)(j)
        val cell = if (v > 0) Cell(v, v, Set(v)) else Cell(0, 0, allCandidates)
        map += (i + 1, j + 1) -> cell
      }
    }
    map
  }
}

/**
  * The Board describes the physical layout of the puzzle.
  * The number of Cells in the board is n^2 * n^2, but there are n * n big cells.
  * This implementation is based on Peter Norvig's algorithm - http://norvig.com/sudoku.html
  * @param cells map from location to Cell giving initially set values
  * @author Barry Becker
  */
case class Board(cells: Map[Location, Cell]) {

  val edgeLength: Int = Math.sqrt(cells.size).toInt
  val baseSize: Int = Math.sqrt(edgeLength).toInt
  private[model] val comps = COMPONENTS(baseSize)
  val numCells: Int = edgeLength * edgeLength

  def this(initial: Array[Array[Int]]) = {
    this(Board.arrayToMap(initial))
  }

  def this(baseSize: Int) = this(Array.ofDim[Int](baseSize * baseSize, baseSize * baseSize))

  def getCell(location: Location): Cell = cells(location)

  def reset(): Board = {
    val candidates = comps.digits.toSet
    new Board(cells.map(c => (c._1, c._2.setCandidateValues(candidates))))
  }

  /** @return true if the board has been successfully solved. Solved if all candidates have a single value. */
  def isSolved: Boolean = cells.values.forall(_.candidateValues.size == 1)
  def getValues(location: Location): Seq[Int] = cells(location).candidateValues.toList

  def getValue(location: Location): Int = {
    cells(location) match {
      case singleValue if singleValue.candidateValues.size == 1 => singleValue.candidateValues.head
      case _ => 0
    }
  }

  /** Sets the original value, and update the list of candidates accordingly */
  def setOriginalValue(location: Location, v: Int): Board = {
    val updatedCells = cells + (location -> cells(location).setValue(v))
    Board(updatedCells)
  }

  /** Remove specified value if it does not prevent the puzzle from being solved using just base consistency check.
    * @return the updated board, or None if not possible to remove value */
  def removeValueIfPossible(location: Location, refresh: Option[Board => Unit] = None): Option[Board] = {
    val initialCells = cells + (location -> Cell(0, 0, comps.digits.toSet))

    val board = Board(initialCells)
    val updatedBoard = board.updateFromInitialData()

    if (updatedBoard.isDefined && updatedBoard.get.isSolved && refresh.isDefined) {
      refresh.foreach(f => f(updatedBoard.get)) // better way to write this?
    }
    updatedBoard
  }

  /** @return the solved board, or None if not solved */
  def solve(refresh: Option[Board => Unit] = None): Option[Board] =
    Solver(this, refresh).solve()

  /*
  def setSolvedValues(): Board = {
    var newBoard: Board = this
    for ((s, cell) <- cells) {
      if (cell.candidateValues.size == 1) {
        newBoard = Board(cells.updated(s, cells(s).setValue(cell.candidateValues.head)))
      }
    }
    newBoard
  }*/

  /** @return the new board if updated successfully, else None if there was an inconsistency. */
  def updateFromInitialData(): Option[Board] = {

    var localValuesMap: ValueMap = this.valuesMap
    for (r <- comps.digits; c <- comps.digits; v = cells((r, c)).originalValue; if v > 0) {
      assign(localValuesMap, (r, c), v) match {
        case Some(values) => localValuesMap = values
        case None => return None
      }
    }
    Some(setValuesMap(localValuesMap))
  }

  def valuesMap: ValueMap = {
    cells.map({case (loc: Location, cell: Cell) => loc -> cell.candidateValues})
  }

  def setValuesMap(valuesMap: ValueMap): Board = {
    val newCells = cells.map({
      case (loc: Location, cell: Cell) => loc -> cell.setCandidateValues(valuesMap(loc))
    })
    Board(newCells)
  }

  /*
  def doRefresh(refresh: Option[(Board) => Unit]): Unit = {
    if (refresh.isDefined) {
      val solvedBoard = setSolvedValues()
      refresh.get(solvedBoard)
    }
  }*/

  /** Assign a value, d, to a square if possible.
    * Eliminate all the other values (except d) from values[s] and propagate.
    * @return Some(values), or None if a contradiction is detected.
    */
  def assign(values: ValueMap, s: Location, d: Int): Option[ValueMap] = {
    val otherValues: Set[Int] = values(s) - d
    var newValues: Option[ValueMap] = Some(values)
    for (d2 <- otherValues) {
      newValues = eliminate(newValues.get, s, d2)
      if (newValues.isEmpty) return None
    }
    newValues
  }

  /** Eliminate d from values[s]; propagate when values or places == 1.
    * @return Some(values), or None if a contradiction is detected.
    */
  private def eliminate(values: ValueMap, s: Location, d: Int): Option[ValueMap] = {

    if (!values(s).contains(d)) return Some(values)
    var newValues = values.updated(s, values(s) - d)
    // If a square s is reduced to one value, d2, then eliminate d2 from the peers.
    if (newValues(s).isEmpty)
      return None // Contradiction
    else if (newValues(s).size == 1) {
      val d2 = newValues(s).head
      for (s2 <- comps.peers(s)) {
        eliminate(newValues, s2, d2) match {   // Recursive call
          case Some(vals) => newValues = vals
          case None =>
            return None // Contradiction
        }
      }
    }
    // If a unit u is reduced to only one place for a value d, then put it there.
    for (u <- comps.units(s)) {
      val dplaces = for (s <- u; if newValues(s).contains(d)) yield s
      if (dplaces.isEmpty)
        return None // Contradiction
      if (dplaces.size == 1) {
        // d can only be in one place in unit; assign it there
        assign(newValues, dplaces.head, d) match {
          case Some(vals) => newValues = vals
          case None =>
            return None  // Contradiction
        }
      }
    }
    Some(newValues)
  }

  override def toString: String = BoardSerializer(this).serialize
  def toDebugString: String = BoardSerializer(this).debugSerialize
}
