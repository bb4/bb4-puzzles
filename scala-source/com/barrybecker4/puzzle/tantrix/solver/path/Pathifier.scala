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
    val newList: mutable.ListBuffer[TilePlacement] = mutable.ListBuffer()
    val seenLoc = mutable.Set.empty[Location]
    val lastAdded: TilePlacement = tileList.head
    val remainder: Seq[TilePlacement] = tileList.tail

    newList.append(lastAdded)
    seenLoc += lastAdded.location
    var outgoing: List[Location] = lastAdded.getOutgoingPathLocations(primaryPathColor).values.toList
    addForwardTiles(newList, seenLoc, outgoing.head, remainder, tantrix)
    outgoing = outgoing.tail
    if (outgoing.nonEmpty) {
      addBackwardTiles(newList, seenLoc, outgoing.head, remainder, tantrix)
      outgoing = outgoing.tail
    }
    if (newList.size != tantrix.size) {
      throw new IllegalStateException ("Did not find a " + primaryPathColor + " path among " + remainder)
    }
    newList.toSeq
  }

  private def addForwardTiles(newList: mutable.ListBuffer[TilePlacement], seenLoc: mutable.Set[Location],
                              outLocation: Location, remaining: Seq[TilePlacement], tantrix: Tantrix): Unit = {
    addTiles(newList, seenLoc, outLocation, remaining, tantrix, forward = true)
  }

  private def addBackwardTiles(newList: mutable.ListBuffer[TilePlacement], seenLoc: mutable.Set[Location],
                               outLocation: Location, remaining: Seq[TilePlacement], tantrix: Tantrix): Unit = {
    addTiles(newList, seenLoc, outLocation, remaining, tantrix, forward = false)
  }

  /** add forward or backward tiles to the beginning or end of path respectively */
  private def addTiles(newList: mutable.ListBuffer[TilePlacement], seenLoc: mutable.Set[Location],
                       outLocation: Location, remaining: Seq[TilePlacement], tantrix: Tantrix, forward: Boolean): Unit = {
    val nextPlacement: Option[TilePlacement] = tantrix(outLocation)
    if (nextPlacement.isDefined && !seenLoc.contains(nextPlacement.get.location) && remaining.nonEmpty) {
      val placement = nextPlacement.get
      if (forward) newList.append(placement)
      else newList.prepend(placement)
      seenLoc += placement.location

      val remainingAfterFilter = remaining.filter(_ != placement)
      val outgoing: List[Location] = placement.getOutgoingPathLocations(primaryPathColor).values.toList

      for (loc <- outgoing)
        addTiles(newList, seenLoc, loc, remainingAfterFilter, tantrix, forward)
    }
  }
}
