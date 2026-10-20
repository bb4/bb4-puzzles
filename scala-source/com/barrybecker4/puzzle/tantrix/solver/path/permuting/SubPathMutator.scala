// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path.permuting

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.TilePlacement
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath

import scala.collection.mutable.ListBuffer


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

  protected def fits(currentPlacement: TilePlacement, previousPlacement: TilePlacement): Boolean =
    currentPlacement.hasPathTo(primaryColor, previousPlacement.location)

  /**
    * Lay out tiles one after another along the primary path, starting from a tile that has already been
    * positioned. Each remaining tile is rotated by the same amount as the first one was,
    * and is placed where the primary path leaves the tile before it.
    *
    * @param first         the already positioned and rotated first tile of the chain
    * @param pivotLocation location of the pivot tile that the first tile is attached to
    * @param remaining     the rest of the tiles in the order that they follow the first one (not yet moved)
    * @param numRotations  how many times each of the remaining tiles is rotated from its original rotation
    * @param action        description of what is being done, for the error message if a tile does not fit.
    * @return the placements of all the tiles in the chain, starting with the first.
    */
  protected def layOutChain(first: TilePlacement, pivotLocation: Location, remaining: Seq[TilePlacement],
                            numRotations: Int, action: => String): Seq[TilePlacement] = {
    val chain = ListBuffer(first)
    var previous = first
    var attachedLocation = pivotLocation
    for (tile <- remaining) {
      val location = findOtherOutgoingLocation(previous, attachedLocation).get
      val current = TilePlacement(tile.tile, location, tile.rotation.rotateBy(numRotations))
      assert(fits(current, previous), s" current=$current did not fit with $previous $action")
      chain.append(current)
      attachedLocation = previous.location
      previous = current
    }
    chain.toSeq
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
    outgoingPathLocations.valuesIterator.find(_ != excludeLocation)
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
    outgoingPathLocations.iterator.collectFirst { case (rot, loc) if loc == location => rot } match
      case Some(rot) => rot
      case None =>
        assert(assertion = false, s"$location was not on an outgoing path from $sourcePlacement")
        -1
  }
}
