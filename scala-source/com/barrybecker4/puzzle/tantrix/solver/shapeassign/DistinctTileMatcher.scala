// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

object DistinctTileMatcher {
  /** Used in place of a cell number for a tile that no cell has. */
  private val NO_CELL = -1
}

/**
  * Checks that the cells that have been placed can all be given different tiles from their domains. This is a bipartite
  * matching of cells to tiles. It is found by repeatedly giving a cell a tile, moving the cell that had the tile on
  * to another one of its tiles if there is one.
  */
class DistinctTileMatcher {
  import DistinctTileMatcher.*

  /** The cell that currently has each tile, or [[NO_CELL]] if the tile is not used. */
  private val cellOfTile = new Array[Int](OptionSet.MAX_TILES)

  /** The tiles that have been looked at while trying to find a tile for one cell, with a bit for each. A tile is only looked
    * at once, which stops cells from passing the same tiles back and forth.
    */
  private var visitedTiles = 0L

  /** @param lastCell the last of the cells to check, from the second cell on, since the first one has its tile already */
  def canAssignDistinctTiles(domains: Array[Long], lastCell: Int): Boolean = {
    java.util.Arrays.fill(cellOfTile, NO_CELL)
    (LoopCells.FIRST_NEW_CELL to lastCell).forall { cell =>
      visitedTiles = 0L
      findTileFor(domains, cell)
    }
  }

  /** Give the cell a tile from its domain, moving other cells to other tiles if that is what it takes.
    * @return false if there is no way to do that
    */
  private def findTileFor(domains: Array[Long], cell: Int): Boolean =
    OptionSet.exists(OptionSet.tilesOf(domains(cell)))(option => tryToTake(domains, cell, OptionSet.tileOf(option)))

  /** Try to give the tile to the cell. A tile that has already been looked at while finding a tile for this cell is skipped. */
  private def tryToTake(domains: Array[Long], cell: Int, tile: Int): Boolean =
    if (hasBeenVisited(tile)) false
    else {
      markVisited(tile)
      takeIfFreeOrOwnerCanMove(domains, cell, tile)
    }

  /** Give the tile to the cell if no cell has it, or if the cell that has it can use another tile. */
  private def takeIfFreeOrOwnerCanMove(domains: Array[Long], cell: Int, tile: Int): Boolean = {
    val owner = cellOfTile(tile)
    val available = owner == NO_CELL || findTileFor(domains, owner)
    if (available) cellOfTile(tile) = cell
    available
  }

  private def hasBeenVisited(tile: Int): Boolean = (visitedTiles & tileBit(tile)) != 0L
  private def markVisited(tile: Int): Unit = visitedTiles |= tileBit(tile)

  /** @return a long with only the bit for the tile switched on. */
  private def tileBit(tile: Int): Long = 1L << tile
}
