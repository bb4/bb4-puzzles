// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

/**
  * A sliding puzzle position that is changed in place, together with a running lower bound on the number of slides
  * left. The bound is the Manhattan distance of every tile plus the linear conflicts in every row and column.
  * Sliding a tile only changes the distance of that tile, and the conflicts in the rows and columns the tile
  * leaves or enters, so only those are recomputed.
  *
  * Sliding a tile into the blank is its own undo: slide the same tile (now where the blank was) back.
  * @param geometry the shape of the puzzle
  * @param startTiles the tile in each cell, row by row, with 0 for the blank
  * @author Barry Becker
  */
final class SliderState(geometry: GridGeometry, startTiles: Seq[Int]) {

  private val tiles: Array[Int] = startTiles.toArray
  private val conflicts = new LineConflicts(geometry)
  private val rowExtra = Array.tabulate(geometry.size)(conflicts.rowExtra(tiles, _))
  private val colExtra = Array.tabulate(geometry.size)(conflicts.colExtra(tiles, _))
  private var manhattanTotal = tiles.indices.map(cell => geometry.distanceToGoal(tiles(cell), cell)).sum
  private var conflictTotal = rowExtra.sum + colExtra.sum

  /** The cell that has no tile in it. */
  var blankCell: Int = tiles.indexOf(0)

  /** @return a lower bound on the number of slides it takes to solve from here. It is 0 only when solved. */
  def estimate: Int = manhattanTotal + conflictTotal

  def tileAt(cell: Int): Int = tiles(cell)

  /** Moves the tile in `cell` (which must be next to the blank) into the blank. The blank takes its place. */
  def slideIntoBlank(cell: Int): Unit = {
    val tile = tiles(cell)
    val oldBlank = blankCell
    manhattanTotal += geometry.distanceToGoal(tile, oldBlank) - geometry.distanceToGoal(tile, cell)
    tiles(oldBlank) = tile
    tiles(cell) = 0
    blankCell = cell
    updateConflicts(tile, cell, oldBlank)
  }

  /**
    * Recomputes the row and the columns that the tile left or entered. Only a line that the tile belongs in can have
    * its conflicts changed by the tile moving, since the blank and the order of the other tiles did not change.
    */
  private def updateConflicts(tile: Int, fromCell: Int, toCell: Int): Unit = {
    val goalRow = geometry.row(geometry.goalCell(tile))
    val goalCol = geometry.col(geometry.goalCell(tile))
    if (goalRow == geometry.row(fromCell) || goalRow == geometry.row(toCell)) refreshRow(goalRow)
    if (goalCol == geometry.col(fromCell) || goalCol == geometry.col(toCell)) refreshCol(goalCol)
  }

  private def refreshRow(row: Int): Unit = {
    val updated = conflicts.rowExtra(tiles, row)
    conflictTotal += updated - rowExtra(row)
    rowExtra(row) = updated
  }

  private def refreshCol(col: Int): Unit = {
    val updated = conflicts.colExtra(tiles, col)
    conflictTotal += updated - colExtra(col)
    colExtra(col) = updated
  }
}
