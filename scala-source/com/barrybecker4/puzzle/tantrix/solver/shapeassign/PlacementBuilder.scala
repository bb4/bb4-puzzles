// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.{Rotation, TilePlacement}

/** Turns the cells of a finished loop, with their single options, into tile placements on the tantrix. */
class PlacementBuilder(grid: HexGrid, cells: LoopCells, options: CellOptions, tiles: LoopTiles, startLocation: Location) {

  /** @return the placements of all the tiles after the first, in the order they are around the loop. */
  def placementsFor(decidedDomains: Array[Long]): Seq[TilePlacement] =
    (1 until tiles.count).map(cell => placementOf(cell, OptionSet.firstOption(decidedDomains(cell))))

  private def placementOf(cell: Int, option: Int): TilePlacement =
    TilePlacement(tiles.tile(OptionSet.tileOf(option)), grid.toLocation(cells.position(cell), startLocation),
      Rotation.values(options.rotationOf(cell, option)))
}
