// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.analysis.fitting

import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.{Tantrix, TilePlacement}

/**
  * Used to check the consistency of all the paths.
  * The process of finding neighbors is a bit more efficient if we have the tantrix.
  *
  * @author Barry Becker
  */
class TantrixTileFitter(var tantrix: Tantrix, primaryColor: PathColor) extends TileFitter(tantrix.tiles, primaryColor) {

  /**
    * @param currentPlacement where we are now
    * @param direction        side to navigate to to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile.
    */
  override protected def getNeighbor(currentPlacement: Option[TilePlacement], direction: Int): Option[TilePlacement] =
    tantrix.getNeighbor(currentPlacement, direction)
}
