// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.puzzle.tantrix.model.PathColor

object HexTile {
  /** number of sides on a hex tile. */
  val NUM_SIDES = 6
}

/** Represents a single tantrix tile. Immutable.
  * @param tantrixNumber the number on the back of the tile
  * @param primaryColor  The primary path color on the back of the tile
  * @param edgeColors outgoing path colors for each orientation index
  * @author Barry Becker
  */
case class HexTile(tantrixNumber: Byte, primaryColor: PathColor, edgeColors: PathColors) {
  override def toString: String = "tileNum=" + tantrixNumber + " colors: " + edgeColors
}
