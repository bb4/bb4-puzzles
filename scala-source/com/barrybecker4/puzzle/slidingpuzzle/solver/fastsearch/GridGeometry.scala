// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.solver.fastsearch

/**
  * Fixed facts about a square sliding puzzle with the blank in the last cell when solved. Cells are numbered
  * row by row, so cell = row * size + col. Tile t (1 to size*size-1) belongs in cell t-1, and the blank is tile 0.
  * Everything is looked up in arrays so that the search does not do any division or allocation.
  * @param size the number of cells along one edge
  * @author Barry Becker
  */
final class GridGeometry(val size: Int) {

  val numCells: Int = size * size

  /** The cell the blank is in when the puzzle is solved. */
  val goalBlankCell: Int = numCells - 1

  private val rowOfCell = Array.tabulate(numCells)(_ / size)
  private val colOfCell = Array.tabulate(numCells)(_ % size)

  /** The cells that share an edge with each cell. */
  val neighbors: Array[Array[Int]] = Array.tabulate(numCells)(neighborsOf)

  private val distances = Array.tabulate(numCells * numCells)(i => manhattan(i / numCells, i % numCells))

  def row(cell: Int): Int = rowOfCell(cell)
  def col(cell: Int): Int = colOfCell(cell)

  /** @return the cell where the tile belongs when the puzzle is solved (the blank belongs in the last cell). */
  def goalCell(tile: Int): Int = if (tile == 0) goalBlankCell else tile - 1

  /** @return how many slides it would take the tile to get from the cell to the cell where it belongs, if it were alone. */
  def distanceToGoal(tile: Int, cell: Int): Int = distances(tile * numCells + cell)

  private def neighborsOf(cell: Int): Array[Int] = {
    val r = rowOfCell(cell)
    val c = colOfCell(cell)
    val candidates = Seq((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
    candidates.filter((nr, nc) => nr >= 0 && nr < size && nc >= 0 && nc < size).map((nr, nc) => nr * size + nc).toArray
  }

  private def manhattan(tile: Int, cell: Int): Int =
    if (tile == 0) 0
    else Math.abs(rowOfCell(goalCell(tile)) - rowOfCell(cell)) + Math.abs(colOfCell(goalCell(tile)) - colOfCell(cell))
}
