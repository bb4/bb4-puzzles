package com.barrybecker4.puzzle.tantrix.solver.verification

import com.barrybecker4.puzzle.tantrix.model.{HexTile, HexUtil}
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

case class CompactnessDetector() {

  /** First add all the tiles to a hash keyed on location.
    * Then for every one of the six sides of each tile, add one if the
    * neighbor is in the hash. Return (num nbrs in hash - 2(numTiles-1))/numTiles
    *
    * @param path the path to determine compactness of.
    * @return measure of path compactness between 0 and ~1
    */
  def determineCompactness(path: TantrixPath): Double = {
    val locationHash = path.tiles.map(_.location).toSet
    val numTiles = path.size

    var ct = 0
    for (p <- path.tiles) {
      for (i <- 0 until HexTile.NUM_SIDES) {
        if (locationHash.contains(HexUtil.getNeighborLocation(p.location, i)))
          ct += 1
      }
    }
    (ct - 2.0 * (numTiles - 1)) / numTiles * 0.5
  }
}
