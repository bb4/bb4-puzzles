// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath


/**
  * Given a TantrixPath and a pivot tile index, find the permuted paths.
  * Since there are 8 total ways to permute and the path already represents one of them,
  * the permuter will never return more than 7 valid permuted paths.
  *
  * @author Barry Becker
  */
abstract class SubPathMutator private[permuting](var primaryColor: PathColor) {
  /**
    * Do something to mutate this subpath - like swap or reverse it.
    *
    * @param subPath the subpath to reverse relative to the pivot tile.
    * @return the mutated subpath.
    */
  def mutate(pivotTile: TilePlacement, subPath: TantrixPath): TantrixPath

  protected def fits(currentPlacement: TilePlacement, previousPlacement: TilePlacement): Boolean = {
    val outgoingPathLocations = currentPlacement.getOutgoingPathLocations(primaryColor)
    for (loc <- outgoingPathLocations.values) {
      if (loc == previousPlacement.location) return true
    }
    false
  }

  /**
    * Of the two outgoing path locations coming out from previousPlacement pick the one that is not the excludeLocation.
    *
    * @param sourcePlacement the tile to consider outgoing paths from.
    * @param excludeLocation we want to chose the other location that is not this when leaving the source.
    * @return the other outgoing location for the sourcePlacement.
    */
  private[permuting] def findOtherOutgoingLocation(sourcePlacement: TilePlacement,
                                                   excludeLocation: Location): Option[Location] = {
    val outgoingPathLocations = sourcePlacement.getOutgoingPathLocations(primaryColor)
    var loc: Option[Location] = None
    for (rot <- outgoingPathLocations.keySet) {
      loc = outgoingPathLocations.get(rot)
      if (loc.get != excludeLocation) return loc
    }
    loc
  }

  /**
    * Find the direction to the specified outgoing location.
    *
    * @param sourcePlacement the tile to consider outgoing paths from.
    * @param location        we want to chose the direction to this location.
    * @return the other outgoing location for the sourcePlacement.
    */
  protected def findOutgoingDirection(sourcePlacement: TilePlacement, location: Location): Int = {
    val outgoingPathLocations = sourcePlacement.getOutgoingPathLocations(primaryColor)
    for (rot <- outgoingPathLocations.keySet) {
      if (outgoingPathLocations(rot) == location) return rot
    }
    assert(assertion = false, s"$location was not on an outgoing path from $sourcePlacement")
    -1
  }
}
