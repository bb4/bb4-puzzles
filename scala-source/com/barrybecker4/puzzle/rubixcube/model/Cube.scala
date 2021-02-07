// Copyright by Barry G. Becker, 2021. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.rubixcube.model

import com.barrybecker4.math.MathUtil
import CubeComponents.{COMPONENTS, numMinicubesToBaseSize}

import scala.util.Random

/**
  * Immutable representation of a Rubix cube.
  *
  * @param locationToMinicube map from position to minicube. Size is the edge length. If size = 3, then there will be 26 entries.
  */
case class Cube(locationToMinicube: Map[(Int, Int, Int), Minicube]) {

  val size: Int = numMinicubesToBaseSize(locationToMinicube.size)

  private val comps = COMPONENTS(size)

  def this(size: Int = 3) {
    this(COMPONENTS(size).initialCubeMap)
  }

  /** shuffle the cube tiles */
  def shuffle(rand: Random = MathUtil.RANDOM): Cube = {
    // create CubeShuffler that applies a buch of random rotations to a cube
    //Cube(locationToMinicube.shuffle(getEmptyLocation, rand))
    this
  }

  /** @return number of colors not in the goal state. Faces are arbitrarily assigned the 6 goal colors */
  def distanceToGoal: Int = {
    val faceGoalNum = size * size

    def numOnFaceInGoal(orientation: Orientation, locations: Seq[(Int, Int, Int)]) =
      locations.map(loc => orientation.goalColor() == locationToMinicube(loc).orientationToColor(orientation))

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
    for (loc <- sliceLocations)
      loc2mini += move.rotateMinicube(loc, loc2mini(loc), size)
    Cube(loc2mini)
  }


  override def toString: String =
    "Cube (distanceToGoal: " + distanceToGoal + "):\n" + locationToMinicube.toString
}
