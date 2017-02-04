// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.model.board

import scala.collection.immutable.HashSet


/**
  * A block of n*n cells in a sudoku puzzle.
  *
  * @author Barry Becker
  */
class BigCell(val board: Board, val rowOffset: Int, val colOffset: Int) extends CellSet {

  /** The number which have not yet been used in this big cell. */
  val candidates: Candidates = new Candidates(board.getValuesList)

  /** The number of Cells in the BigCell is n * n.  */
  private val n: Int  = board.getBaseSize

  /** The internal data structures representing the game board. Row, column order. */
  private val cells: Array[Array[Cell]] = Array.ofDim[Cell](n, n)


  for (i <- 0 until n; j <- 0 until n) {
    cells(i)(j) = board.getCell(rowOffset + i, colOffset + j)
    cells(i)(j).setParent(this)
  }

  def numCells: Int = n * n

  /**
    * @return retrieve the base size of the board - sqrt(edge magnitude).
    */
  final def getSize: Int = n

  /** a value has been set, so we need to remove it from all the candidate lists. */
  def removeCandidate(unique: Int) {
    candidates.remove(unique)
    var j = 0
    for (j <- 0 until n; i <- 0 until n)
      getCell(i, j).remove(unique)
  }

  /** add to the bigCell candidate list and each cells candidates for cells not yet set in stone. */
  def addCandidate(value: Int) {
    candidates.add(value)
    clearCaches()
  }

  /** assume all of them, then remove those that are represented. */
  def updateCandidates(values: ValuesList) {
    candidates.clear()
    candidates.addAll(values)
    for (i <- 0 until n; j <- 0 until n) {
      val v = cells(i)(j).getValue
      if (v > 0) candidates.remove(v)
    }
  }

  /**
    * If this bigCell has a row (0, n_-1) that has the only cells with candidates for value,
    * then return that row, else return -1.
    *
    * @param value value
    * @return row (0 to n-1) if found, else -1
    */
  def findUniqueRowFor(value: Int): Int = {
    var rows = new HashSet[Integer]

    for (i <- 0 until n; j <- 0 until n) {
      val cands = getCell(i, j).getCandidates
      if (cands.contains(value)) {
        rows += i
        //break //todo: break is not supported
      }
    }
    if (rows.size == 1) rows.head else -1
  }

  /**
    * If this bigCell has a row (0, n_-1) that has the only cells with candidates for value,
    * then return that col, else return -1.
    *
    * @param value value
    * @return ro (0 to n-1) if found, else -1
    */
  def findUniqueColFor(value: Int): Int = {
    var cols = new HashSet[Integer]

    for {
      j <- 0 until n
      i <- 0 until n
      cands = getCell(i, j).getCandidates
      if cands.contains(value)
    } cols += j  // for performance, we want to break and avoid remaining i's for j loop

    if (cols.size == 1) cols.head else -1
  }

  def getCell(position: Int): Cell = getCell(position / n, position % n)

  /**
    * returns null if there is no game piece at the position specified.
    *
    * @return the piece at the specified location. Returns null if there is no piece there.
    */
  final def getCell(row: Int, col: Int): Cell = {
    assert(row >= 0 && row < n && col >= 0 && col < n)
    cells(row)(col)
  }

  private def clearCaches() = for (j <- 0 until n; i <- 0 until n) getCell(i, j).clearCache()
}
