// Copyright by Barry G. Becker, 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.Box


case class BoundingBoxCalculator() {

    def getBoundingBox(tiles: Seq[TilePlacement]): Box =
      tiles.tail.foldLeft(new Box(tiles.head.location))((bbox, tile) => bbox.expandBy(tile.location))

}
