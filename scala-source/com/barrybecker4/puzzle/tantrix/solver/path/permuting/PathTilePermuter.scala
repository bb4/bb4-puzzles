// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.PrimaryPathFitter
import com.barrybecker4.puzzle.tantrix.model.{HexTile, Rotation, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath
import scala.collection.mutable.ListBuffer


/**
  * Swap tiles in place in a specified originalPath.
  * @author Barry Becker
  */
class PathTilePermuter private[permuting](var originalPath: TantrixPath) {

  private val color: PathColor = originalPath.primaryPathColor

  /**
    * Permutes the tiles at oldIndices to new positions at new Indices
    * @param oldIndices old positions in the path
    * @param newIndices new positions to place the tiles at.
    * @return [[Some]] path if every swapped tile can be rotated to fit at its target hex; [[None]] if not
    *         (e.g. metaheuristic neighbor — skip instead of aborting the whole search).
    */
  private[permuting] def permute(oldIndices: ListBuffer[Int], newIndices: ListBuffer[Int]): Option[TantrixPath] = {
    val auxList: Array[TilePlacement] = Array.ofDim[TilePlacement](oldIndices.size)
    assert(consistent(oldIndices, newIndices))
    for (i <- oldIndices.indices)
      auxList(i) = originalPath.tiles(newIndices(i))
    val fitter = new PrimaryPathFitter(originalPath.tiles, color)
    val origPlacements: ListBuffer[TilePlacement] = ListBuffer.empty ++= originalPath.tiles
    var ok = true
    for (i <- newIndices.indices if ok) {
      val oldIndex = oldIndices(i)
      val oldPlacement = auxList(i)
      findNewPlacement(oldPlacement.tile, origPlacements(oldIndex).location, fitter) match {
        case None         => ok = false
        case Some(placed) => origPlacements(oldIndex) = placed
      }
    }
    if (ok) Some(new TantrixPath(origPlacements.toSeq, originalPath.primaryPathColor, originalPath.desiredLength))
    else None
  }

  private def consistent(oldIndices: ListBuffer[Int], newIndices: ListBuffer[Int]): Boolean = {
    val uniqueVals: Set[Int] = Set.empty ++ oldIndices
    uniqueVals.size == oldIndices.size && newIndices.forall(oldIndices.contains(_))
  }

  /** @return placement with the tile rotated to fit at `location`, or [[None]] if no rotation works. */
  private def findNewPlacement(tile: HexTile, location: Location, fitter: PrimaryPathFitter)
      : Option[TilePlacement] = {
    var newPlacement = TilePlacement(tile, location, Rotation.ANGLE_0)
    var ct = 0
    while (!fitter.isFit(newPlacement) && ct < HexTile.NUM_SIDES) {
      newPlacement = newPlacement.rotate()
      ct += 1
    }
    if (ct >= HexTile.NUM_SIDES) None
    else Some(newPlacement)
  }
}
