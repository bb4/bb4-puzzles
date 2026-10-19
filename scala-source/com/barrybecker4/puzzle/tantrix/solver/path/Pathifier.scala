// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.common.geometry.Location
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.{Tantrix, TilePlacement}

import scala.collection.mutable

/**
  * Attempts to reorder the tiles so that they show a path of the primary color.
  * No rotations are made, if there is a path, we will find it.
  * If there is no path, give an error.
  *
  * The list of tiles that are passed in must be a continuous primary path,
  * but it is not required that it be a loop, or that any of the secondary colors match.
  *
  * @param primaryPathColor primary path color
  * @author Barry Becker
  */
class Pathifier private[path](val primaryPathColor: PathColor)  {
  require (primaryPathColor != null)

  /** Attempt to reorder the tiles into a path if possible.
    * Throw an error if no path. Should not change the order if the tiles are already arranged in a path.
    * @param tantrix the tantrix containing the tiles to reorder.
    * @return the tiles in path order. Error if no path.
    */
  private[path] def reorder (tantrix: Tantrix): Seq[TilePlacement] = {
    val tiles = tantrix.tiles.toList
    if (tantrix.size <= 1) tiles else reorderTiles(tiles, tantrix)
  }

  /** make an ordered path list from the tiles in tileList */
  private def reorderTiles(tileList: List[TilePlacement], tantrix: Tantrix): Seq[TilePlacement] = {
    val start = tileList.head
    val newList = mutable.ListBuffer(start)
    val seenLoc = mutable.Set(start.location)
    // The path leaves the start tile in two directions. One is followed forward, the other backward.
    val outgoing = start.getOutgoingPathLocations(primaryPathColor).values.toList
    addTiles(newList, seenLoc, outgoing.head, tantrix, forward = true)
    outgoing.tail.foreach(addTiles(newList, seenLoc, _, tantrix, forward = false))
    if (newList.size != tantrix.size)
      throw new IllegalStateException(s"Did not find a $primaryPathColor path among ${tileList.tail}")
    newList.toSeq
  }

  /** add forward or backward tiles to the beginning or end of path respectively */
  private def addTiles(newList: mutable.ListBuffer[TilePlacement], seenLoc: mutable.Set[Location],
                       outLocation: Location, tantrix: Tantrix, forward: Boolean): Unit =
    tantrix(outLocation).filterNot(next => seenLoc.contains(next.location)).foreach { placement =>
      if (forward) newList.append(placement) else newList.prepend(placement)
      seenLoc += placement.location
      placement.getOutgoingPathLocations(primaryPathColor).values
        .foreach(addTiles(newList, seenLoc, _, tantrix, forward))
    }
}
