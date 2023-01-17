// Copyright by Barry G. Becker, 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.Box
import scala.Iterable


case class BoundingBoxCalculator() {

    def getBoundingBox(tiles: Seq[TilePlacement]): Box = {
      var bbox = new Box(tiles.head.location)
      tiles.foreach(tile => bbox = bbox.expandBy(tile.location))
      bbox
    }

}
