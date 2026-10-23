// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver.backtracking

import PieceEdgeTable.{BOTTOM, LEFT, RIGHT, ROTATIONS, TOP}

object PlacementSearch {
  /** Stands for "no neighbor, so any nub is fine" where a required nub code is expected. */
  val NO_REQUIREMENT = -1
  /** How many placements are tried between reports of progress (which are slow compared to a placement). */
  val PROGRESS_INTERVAL = 512
}

/**
  * Fills the grid one cell at a time, left to right and then top to bottom. For each cell it tries every unused piece
  * in every rotation, and keeps only the ones whose left and top nubs fit the neighbors that are already there.
  * When no piece fits a cell, it backs up to the previous cell and tries the next possibility there.
  *
  * Nothing is allocated while searching. Which pieces are used is a bit mask, and the placed pieces are slots in
  * an int array, so a placement costs a few array reads and integer compares.
  * @param table the pieces and their rotations
  * @param edgeLength number of pieces in a row of the finished puzzle
  * @param onProgress told the slots placed so far, and the number of placements tried, every PROGRESS_INTERVAL tries.
  *                   It may throw to stop the search.
  */
final class PlacementSearch(table: PieceEdgeTable, edgeLength: Int, onProgress: (Array[Int], Long) => Unit) {
  import PlacementSearch.*

  private val numCells = table.numPieces
  private val placedSlots = new Array[Int](numCells)
  private var placedCount = 0
  private var usedPieces = 0 // bit i is set when piece i is placed
  private var numTries = 0L

  def triesSoFar: Long = numTries

  /** @return the slot of each piece in cell order if there is a way to fill every cell. */
  def solve(): Option[Array[Int]] = if (fillFrom(0)) Some(placedSlots.clone()) else None

  /** @return the slots placed so far, in cell order. */
  def placedSoFar: Array[Int] = placedSlots.take(placedCount)

  private def fillFrom(cell: Int): Boolean = cell == numCells || anyPieceFits(cell)

  private def anyPieceFits(cell: Int): Boolean = {
    val needLeft = nubNeededOnLeft(cell)
    val needTop = nubNeededOnTop(cell)
    var piece = 0
    while (piece < numCells && !tryPiece(cell, piece, needLeft, needTop)) piece += 1
    piece < numCells
  }

  private def tryPiece(cell: Int, piece: Int, needLeft: Int, needTop: Int): Boolean =
    (usedPieces & (1 << piece)) == 0 && anyRotationFits(cell, piece, needLeft, needTop)

  private def anyRotationFits(cell: Int, piece: Int, needLeft: Int, needTop: Int): Boolean = {
    var rotation = 0
    while (rotation < ROTATIONS && !tryRotation(cell, table.slot(piece, rotation), needLeft, needTop)) rotation += 1
    rotation < ROTATIONS
  }

  private def tryRotation(cell: Int, slot: Int, needLeft: Int, needTop: Int): Boolean = {
    countTry()
    matches(table.edge(slot, LEFT), needLeft) && matches(table.edge(slot, TOP), needTop) && placeAndContinue(cell, slot)
  }

  /** Puts the slot in the cell, and takes it out again if the rest of the cells cannot be filled. */
  private def placeAndContinue(cell: Int, slot: Int): Boolean = {
    val pieceBit = 1 << table.pieceOf(slot)
    placedSlots(cell) = slot
    usedPieces |= pieceBit
    placedCount = cell + 1
    val filled = fillFrom(cell + 1)
    if (!filled) { usedPieces &= ~pieceBit; placedCount = cell }
    filled
  }

  private def matches(edge: Int, needed: Int): Boolean = needed == NO_REQUIREMENT || edge == needed

  /** The nub that the left neighbor's right nub fits with, or none for a cell in the first column. */
  private def nubNeededOnLeft(cell: Int): Int =
    if (cell % edgeLength == 0) NO_REQUIREMENT else NubCodes.complement(table.edge(placedSlots(cell - 1), RIGHT))

  /** The nub that the upper neighbor's bottom nub fits with, or none for a cell in the first row. */
  private def nubNeededOnTop(cell: Int): Int =
    if (cell < edgeLength) NO_REQUIREMENT else NubCodes.complement(table.edge(placedSlots(cell - edgeLength), BOTTOM))

  private def countTry(): Unit = {
    numTries += 1
    if (numTries % PROGRESS_INTERVAL == 0) onProgress(placedSoFar, numTries)
  }
}
