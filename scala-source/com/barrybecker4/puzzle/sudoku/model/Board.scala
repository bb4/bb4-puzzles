// Copyright by Barry G. Becker, 2017 - 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model

import BoardComponents.COMPONENTS
import com.barrybecker4.puzzle.sudoku.model.Board.getInitialValuesMap
import scala.collection.immutable.HashMap


object Board {
  private def arrayToMap(initial: Array[Array[Int]]): CellMap = {
    (for (i <- initial.indices; row = initial(i);
          j <- row.indices; v = row(j))
      yield (i + 1, j + 1) -> (if (v > 0) Cell(v, v) else Cell(0, 0))
    ).toMap
  }

  private def getInitialValuesMap(cells: CellMap): ValuesMap = {
    val baseSize = Math.sqrt(Math.sqrt(cells.size)).toInt
     COMPONENTS(baseSize).initialValueMap
  }
}

/**
  * The Board describes the physical layout of the puzzle. It is immutable.
  * The number of Cells in the board is n^2 * n^2, but there are n * n big cells.
  * This implementation is based on Peter Norvig's algorithm - http://norvig.com/sudoku.html
  * @param cells map from location to Cell giving initially set values (immutable)
  * @param valuesMap map from locations to candidate values (immutable)
  * @author Barry Becker
  */
case class Board(cells: CellMap, valuesMap: ValuesMap) {

  val edgeLength: Int = Math.sqrt(cells.size).toInt
  val baseSize: Int = Math.sqrt(edgeLength).toInt
  private[model] val comps = COMPONENTS(baseSize)
  private val valueAssigner: ValueAssigner = ValueAssigner(comps)
  val numCells: Int = edgeLength * edgeLength

  def this(cells: CellMap) {
    this(cells, getInitialValuesMap(cells))
  }
  def this(initial: Array[Array[Int]]) = {
    this(Board.arrayToMap(initial))
  }

  def this(baseSize: Int) = this(Array.ofDim[Int](baseSize * baseSize, baseSize * baseSize))

  def getCell(location: Location): Cell = cells(location)

  def reset(): Board = {
    Board(cells, comps.initialValueMap)
  }

  /** @return true if the board has been successfully solved. Solved if all candidates have a single value. */
  def isSolved: Boolean = valuesMap.values.forall(_.size == 1)
  def getValues(location: Location): Seq[Int] = valuesMap(location).toList

  def getValue(location: Location): Int = {
    valuesMap(location) match {
      case singleValue if singleValue.size == 1 => singleValue.head
      case _ => 0
    }
  }

  /** Sets the original value, and update the list of candidates accordingly */
  def setOriginalValue(location: Location, v: Int): Board = {
    Board(cells + (location -> Cell(v, v)), valuesMap)
  }

  /** Remove specified value if it does not prevent the puzzle from being solved using just base consistency check.
    * @return the updated board, or None if not possible to remove value */
  def removeValueIfPossible(location: Location, refresh: Option[Board => Unit] = None): Option[Board] = {
    val initialCells = cells.updated(location, Cell(0, 0))

    val board = new Board(initialCells)
    val updatedBoard = board.updateFromInitialData()

    if (updatedBoard.isDefined && updatedBoard.get.isSolved && refresh.isDefined) {
      refresh.get(updatedBoard.get) // better way to write this?
      updatedBoard
    }
    else None
  }

  /** @return the solved board, or None if not solved */
  def solve(refresh: Option[Board => Unit] = None): Option[Board] =
    Solver(this, refresh).solve()

  /** @return the new board if updated successfully, else None if there was an inconsistency. */
  def updateFromInitialData(): Option[Board] = {
    var localValuesMap: ValuesMap = this.valuesMap
    for (r <- comps.digits; c <- comps.digits; v = cells((r, c)).originalValue; if v > 0) {
      assign((r, c), v, localValuesMap) match {
        case Some(values) =>
          localValuesMap = values
        case None =>
          return None
      }
    }
    Some(Board(cells, localValuesMap))
  }

  def assign(loc: Location, value: Int, valuesMap: ValuesMap): Option[ValuesMap] =
    valueAssigner.assign(loc, value, valuesMap)

  def setSolvedValues(): Board = {
    val newCells = cells.map({
      case (k, v) =>
        val values = valuesMap(k)
        k -> (if (values.size == 1) Cell(v.originalValue, values.head) else v)
    })
    Board(newCells, valuesMap)
  }

  def doRefresh(refresh: Option[Board => Unit]): Unit = {
    if (refresh.isDefined) {
      refresh.get(setSolvedValues())
    }
  }

  override def toString: String = BoardSerializer(this).serialize
}
