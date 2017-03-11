/*
 * // Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor

object HexTile {
  /** number of sides on a hex tile. */
  val NUM_SIDES = 6
}

object PathColor extends Enumeration {
  type PathColor = Value
  val RED, GREEN, BLUE, YELLOW, WHITE = Value
}


/**
  * Represents a single tantrix tile. Immutable.
  * @author Barry Becker
  */
case class HexTile(tantrixNumber: Byte, primaryColor: PathColor, edgeColors: Array[PathColor]) {

  assert(edgeColors.length == 6)

  /** @return the number on the back of the tile */
  def getTantrixNumber: Byte = tantrixNumber

  /** @return outgoing path color for specified orientation index */
  def getEdgeColor(index: Int): PathColor = edgeColors(index)

  /** @return The primary path color on the back of the tile */
  def getPrimaryColor: PathColor = primaryColor

  override def toString: String = "tileNum=" + tantrixNumber + " colors: " + edgeColors
}
