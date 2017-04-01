// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.PathColor.PathColor
import com.barrybecker4.puzzle.tantrix.model.RotationEnum.Rotation

import scala.collection.immutable.HashMap


/**
  * Represents the positioning of a tantrix tile on the tantrix. Immutable.
  *
  * @author Barry Becker
  */
case class TilePlacement(tile: HexTile, location: Location, rotation: Rotation) {
  assert(location != null)
  assert(rotation != null)
  assert(tile != null)

  def getPathColor(i: Int): PathColor = {
    var index = (i - rotation.ordinal) % NUM_SIDES
    index = if (index < 0) index + NUM_SIDES
    else index
    tile.edgeColors(index)
  }

  /** @return new tile placement that is the old tile placement rotated counter-clockwise once. */
  def rotate(): TilePlacement = TilePlacement(tile, location, rotation.rotateBy(1))

  /**
    * @param primaryColor color of the path to get locations for
    * @return map from outgoing path index to corresponding location for paths of the specified color
    */
  def getOutgoingPathLocations(primaryColor: PathColor): Map[Int, Location] = {
    var outgoingPathLocations = new HashMap[Int, Location]
    for (i <- 0 until NUM_SIDES) {
      if (primaryColor == getPathColor(i))
        outgoingPathLocations += (i -> HexUtil.getNeighborLocation(location, i))
    }
    assert(outgoingPathLocations.size == 2,
      "Must always be two paths. Instead had " + outgoingPathLocations + " for " + this + " pcolor=" + primaryColor)
    outgoingPathLocations
  }

  override def toString: String = "[" + tile + " at " + location + " " + rotation + "]"
}