// Copyright by Barry G. Becker, 2017 - 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.puzzle.tantrix.model.HexTile
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.solver.path.PathType
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.collection.mutable.ListBuffer

/**
  * A list of indices that point to tiles having a specified type within the primary path.
  *
  * @param pathType type of the arc on a tile.
  * @param originalPath tantrix primary path of a single color.
  * @author Barry Becker
  */
class TilesOfTypeIndices private[permuting](val pathType: PathType, val originalPath: TantrixPath) {
  val list: ListBuffer[Int] = ListBuffer()
  initialize(pathType, originalPath)
  private var primColor: PathColor = _
  def size: Int = list.size

  private def initialize(pathType: PathType, path: TantrixPath): Unit = {
    val tiles = path.tiles
    primColor = path.primaryPathColor
    for (i <- 0 until path.size) {
        val tile = tiles(i).tile
        if (isTileType(tile, pathType)) list.append(i)
    }
  }

  private def isTileType(tile: HexTile, pathType: PathType) = {
    val pathEdges = new ListBuffer[Integer]
    var ct = 0
    while (pathEdges.size < 2) {
      if (tile.edgeColors(ct) == primColor) pathEdges.append(ct)
      ct += 1
    }
    var diff = Math.abs(pathEdges.head - pathEdges(1))
    diff = if (diff == 5) 1
    else diff
    diff = if (diff == 4) 2
    else diff
    diff == (pathType.ordinal + 1)
  }
}
