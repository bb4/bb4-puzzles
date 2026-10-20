// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

/**
  * Checks that the cells that have been placed can all be given different tiles from their domains. This is a bipartite
  * matching of cells to tiles. It is found by repeatedly giving a cell a tile, moving the cell that had the tile on
  * to another one of its tiles if there is one.
  */
class DistinctTileMatcher {

  private val cellOfTile = new Array[Int](OptionSet.MAX_TILES)
  private var visitedTiles = 0L

  /** @param lastCell the last of the cells to check, from the second cell on, since the first one has its tile already */
  def canAssignDistinctTiles(domains: Array[Long], lastCell: Int): Boolean = {
    java.util.Arrays.fill(cellOfTile, -1)
    (1 to lastCell).forall { cell =>
      visitedTiles = 0L
      assignTile(domains, cell)
    }
  }

  private def assignTile(domains: Array[Long], cell: Int): Boolean =
    OptionSet.exists(OptionSet.tilesOf(domains(cell)))(option => tryTile(domains, cell, OptionSet.tileOf(option)))

  private def tryTile(domains: Array[Long], cell: Int, tile: Int): Boolean =
    (visitedTiles & (1L << tile)) == 0L && { visitedTiles |= 1L << tile; claimTile(domains, cell, tile) }

  /** Give the tile to the cell if no cell has it, or if the cell that has it can use another tile. */
  private def claimTile(domains: Array[Long], cell: Int, tile: Int): Boolean = {
    val available = cellOfTile(tile) < 0 || assignTile(domains, cellOfTile(tile))
    if (available) cellOfTile(tile) = cell
    available
  }
}
