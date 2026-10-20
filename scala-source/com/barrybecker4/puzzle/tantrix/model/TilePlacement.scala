// Copyright by Barry G. Becker, 2017 - 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.HexTile.NUM_SIDES
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.Rotation

import scala.collection.immutable.HashMap


/**
  * Represents the positioning of a tantrix tile on the tantrix. Immutable.
  * @author Barry Becker
  */
case class TilePlacement(tile: HexTile, location: Location, rotation: Rotation) {
  assert(location != null)
  assert(rotation != null)
  assert(tile != null)

  def getPathColor(i: Int): PathColor = tile.edgeColors((NUM_SIDES + i - rotation.ordinal) % NUM_SIDES)

  /** @return new tile placement that is the old tile placement rotated counter-clockwise once. */
  def rotate(): TilePlacement = TilePlacement(tile, location, rotation.rotateBy(1))

  /** @param primaryColor color of the path to get locations for
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

  /** @param primaryColor color of the path to follow
    * @param target       location to check
    * @return true if one of the two outgoing paths of the specified color leads to the target location.
    */
  def hasPathTo(primaryColor: PathColor, target: Location): Boolean = {
    // Does the same as checking getOutgoingPathLocations, but without building a map. This is checked
    // for every pair of tiles every time a path is created, so it matters.
    var numPaths = 0
    var leadsToTarget = false
    var i = 0
    while (i < NUM_SIDES) {
      if (primaryColor == getPathColor(i)) {
        numPaths += 1
        if (HexUtil.getNeighborLocation(location, i) == target) leadsToTarget = true
      }
      i += 1
    }
    assert(numPaths == 2, "Must always be two paths. Instead had " + numPaths + " for " + this + " pcolor=" + primaryColor)
    leadsToTarget
  }

  override def toString: String = "[" + tile + " at " + location + " " + rotation + "]"
}
