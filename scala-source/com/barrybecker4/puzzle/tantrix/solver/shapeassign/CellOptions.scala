// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

/**
  * The options for each cell of the loop: the tiles and rotations that have the primary path where the shape of the loop
  * needs it to be. What is remembered about an option is its rotation and the colors of the six sides, which
  * is what is needed to check that cells that touch have sides that match.
  */
class CellOptions(numCells: Int, tiles: LoopTiles) {

  // Both are indexed by the cell, and then by the option number (see [[OptionSet]]), not by the rotation.
  private val rotations = Array.ofDim[Byte](numCells, OptionSet.MAX_OPTIONS)
  private val sideColors = Array.ofDim[Int](numCells, OptionSet.MAX_OPTIONS)

  def rotationOf(cell: Int, option: Int): Int = rotations(cell)(option)

  /** @return the color number of the side of the cell when it has the option. */
  def colorOnSide(cell: Int, option: Int, side: Int): Int = 
    (sideColors(cell)(option) >> (LoopTiles.BITS_PER_COLOR * side)) & LoopTiles.COLOR_MASK

  /** The first cell has only one option, which is its tile as it is already placed. */
  def setPlacedTile(rotation: Int): Long = record(LoopCells.START_CELL, LoopTiles.PLACED_TILE, 0, rotation)

  /** @return the options for the cell, which are the ways to place tiles of its kind so that the primary path enters and leaves as needed. */
  def optionsFor(cell: Int, entrySide: Int, exitSide: Int): Long = {
    val kind = LoopKind.kindOf(LoopKind.sidesBetween(entrySide, exitSide))
    OptionSet.unionOverRange(tiles.count) { tile =>
      if (tile >= LoopTiles.FIRST_NEW_TILE && tiles.kind(tile) == kind) optionsOfTile(cell, tile, entrySide, exitSide) else 0L
    }
  }

  private def optionsOfTile(cell: Int, tile: Int, entrySide: Int, exitSide: Int): Long = {
    val fittingRotations = (0 until HexGrid.NUM_SIDES).filter(tiles.fits(tile, _, entrySide, exitSide))
    OptionSet.unionOverRange(fittingRotations.size)(number => record(cell, tile, number, fittingRotations(number)))
  }

  /** Remember the rotation and the colors for the option. @return the set that has just that option. */
  private def record(cell: Int, tile: Int, rotationNumber: Int, rotation: Int): Long = {
    val option = OptionSet.optionOf(tile, rotationNumber)
    rotations(cell)(option) = rotation.toByte
    sideColors(cell)(option) = tiles.packedColors(tile, rotation)
    OptionSet.single(option)
  }
}
