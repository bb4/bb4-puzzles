// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.math.MathUtil
import CubeComponents.{getCompsForSize, numMinicubesToBaseSize}
import com.barrybecker4.puzzle.rubixcube.Location

import scala.util.Random

/**
  * Immutable representation of a Rubix cube.
  * @param locationToMinicube map from position to minicube.
  *                           Size is the edge length. If size = 3, then there will be 26 entries.
  */
case class Cube(locationToMinicube: Map[Location, Minicube]) {

  val size: Int = numMinicubesToBaseSize(locationToMinicube.size)

  private val comps = getCompsForSize(size)

  def this(size: Int = 3) {
    this(getCompsForSize(size).initialCubeMap)
  }

  /** shuffle the cube tiles */
  def shuffle(rand: Random = MathUtil.RANDOM): Cube =
    new CubeShuffler(rand).shuffle(this)

  def getSlice(orientation: Orientation, level: Int): Map[Location, Minicube] = {
    assert (level <= size)
    val locsForSlice = comps.locationsForSlice(orientation, level)
    var m: Map[Location, Minicube] = Map()
    locsForSlice.foreach(loc => {
      if (locationToMinicube.contains(loc)) {
        m += loc -> locationToMinicube(loc)
      }
    })
    m
  }


  /** @return number of colors not in the goal state. Faces are arbitrarily assigned the 6 goal colors */
  def distanceToGoal: Int = {
    val faceGoalNum = size * size

    def numOnFaceInGoal(orientation: Orientation, locations: Seq[Location]) = {
      val goalColor =
        if (size % 2 == 1) locationToMinicube(locations(locations.size / 2)).orientationToColor(orientation)
        else orientation.goalColor()
      locations.map(loc => goalColor == locationToMinicube(loc).getColorForOrientation(orientation))
    }

    val numInGoal: Int =
      comps.faceToLocations
        .flatMap({ case (orientation, locations) => numOnFaceInGoal(orientation, locations) })
        .count(v => v)

    6 * faceGoalNum - numInGoal
  }

  /** @return true if all the tiles, when read across and down, are in increasing order. */
  def isSolved: Boolean = distanceToGoal == 0

  /** Creates a new cube with the move applied. */
  def doMove(move: CubeMove): Cube = {
    val sliceLocations = comps.sliceLocations((move.orientation, move.level))
    var loc2mini = locationToMinicube
    var slice: Map[Location, Minicube] = Map()

    for (loc <- sliceLocations)
      slice += loc -> locationToMinicube(loc)

    for (loc <- sliceLocations)
      loc2mini += move.rotateMinicube(loc, slice(loc), size)

    Cube(loc2mini)
  }

  override def toString: String =
    "Cube (distanceToGoal: " + distanceToGoal + "):\n" + locationToMinicube.toString
}
