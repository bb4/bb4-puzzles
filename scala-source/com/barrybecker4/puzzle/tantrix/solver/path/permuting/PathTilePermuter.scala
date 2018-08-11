// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.analysis.fitting.PrimaryPathFitter
import com.barrybecker4.puzzle.tantrix.model.{HexTile, RotationEnum, TilePlacement}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.collection.mutable.ListBuffer


/**
  * Swap tiles in place in a specified originalPath.
  *
  * @author Barry Becker
  */
class PathTilePermuter private[permuting](var originalPath: TantrixPath) {

  private var color: PathColor = originalPath.primaryPathColor

  /**
    * Permutes the tiles at oldIndices to new positions at new Indices
    *
    * @param oldIndices old positions in the path
    * @param newIndices new positions to place the tiles at.
    * @return the new rearranged path.
    */
  private[permuting] def permute(oldIndices: ListBuffer[Int], newIndices: ListBuffer[Int]) = {
    //val permutedPath = originalPath.copy
    val auxList: Array[TilePlacement] = Array.ofDim[TilePlacement](oldIndices.size)
    assert(consistent(oldIndices, newIndices))
    for (i <- oldIndices.indices)
      auxList(i) = originalPath.tiles(newIndices(i))
    val fitter = new PrimaryPathFitter(originalPath.tiles, color)
    val origPlacements: ListBuffer[TilePlacement] = originalPath.tiles.to[ListBuffer]
    for (i <- newIndices.indices) {
      val oldIndex = oldIndices(i)
      val oldPlacement = auxList(i)
      val newPlacement = findNewPlacement(oldPlacement.tile, origPlacements(oldIndex).location, fitter)
      origPlacements(oldIndex) = newPlacement
    }
    new TantrixPath(origPlacements, originalPath.primaryPathColor) //permutedPath
  }

  private def consistent(oldIndices: ListBuffer[Int], newIndices: ListBuffer[Int]): Boolean = {
    val uniqueVals: Set[Int] = Set(oldIndices:_*)
    uniqueVals.size == oldIndices.size && newIndices.forall(oldIndices.contains(_))
  }

  /**
    * @return The new placement with the tile rotated so it fits at the new location.
    */
  private def findNewPlacement(tile: HexTile, location: Location, fitter: PrimaryPathFitter) = {
    var newPlacement = TilePlacement(tile, location, RotationEnum.ANGLE_0)
    var ct = 0
    while (!fitter.isFit(newPlacement) && ct < HexTile.NUM_SIDES) {
      newPlacement = newPlacement.rotate()
      ct += 1
    }
    if (ct >= HexTile.NUM_SIDES)
      throw new IllegalStateException("could not fit " + tile + " at " + location + " in " + fitter.getTantrix)
    newPlacement
  }
}
