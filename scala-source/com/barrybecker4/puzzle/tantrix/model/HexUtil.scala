// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import java.awt.geom.Point2D

import com.barrybecker4.common.geometry.{IntLocation, Location}

/**
  * Used to find neighboring locations in hex space.
  * Tiles are arranged like this:
  *    0,0   0,1   0,2
  * 1,0   1,1   1,2   1,3
  *    2,0   2,1   2,2
  */
object HexUtil {

  /** Odd rows are shifted back one.
    * @param loc       source location
    * @param direction side to navigate to to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile.
    */
  def getNeighborLocation(loc: Location, direction: Int): Location = {
    val row = loc.row
    val col = loc.col
    val colOffset = oddRowShift(row)

    direction match {
      case 0 => IntLocation(row, col + 1)
      case 1 => IntLocation(row - 1, col + colOffset + 1)
      case 2 => IntLocation(row - 1, col + colOffset)
      case 3 => IntLocation(row, col - 1)
      case 4 => IntLocation(row + 1, col + colOffset)
      case 5 => IntLocation(row + 1, col + colOffset + 1)
      case _ => throw new IllegalArgumentException("Unexpected direction: " + direction)
    }
  }

  /** Convert to cartesian space, then computer the distance.
    * @param loc1 first location
    * @param loc2 second location
    * @return distance between two hex locations.
    */
  def distanceBetween(loc1: Location, loc2: Location): Double =
    pointFromLocation(loc1).distance(pointFromLocation(loc2))

  /** Number of steps along neighboring tiles to get from one location to another.
    * This is not the same as [[distanceBetween]], which is the straight line distance. Two tiles
    * that are `hexDistance` apart need at least `hexDistance - 1` tiles between them to connect.
    * @return the minimum number of moves between neighbors needed to get from loc1 to loc2.
    */
  def hexDistance(loc1: Location, loc2: Location): Int = {
    val dq = axialColumn(loc1) - axialColumn(loc2)
    val dr = loc1.row - loc2.row
    Math.max(Math.abs(dq), Math.max(Math.abs(dr), Math.abs(dq + dr)))
  }

  /** Column in axial coordinates (where the row axis is unchanged). In axial coordinates, the six neighbor
    * offsets are the same for every tile, which makes it easy to compute distances. Odd rows are shifted back,
    * so that is undone by the floor of (row + 1) / 2.
    */
  private def axialColumn(loc: Location): Int = loc.col - Math.floorDiv(loc.row + 1, 2)

  /** @return -1 if the row is odd (odd rows are shifted back), else 0. Correct for negative rows too. */
  private def oddRowShift(row: Int): Int = if ((row & 1) == 1) -1 else 0

  private def pointFromLocation(loc: Location): Point2D =
    new Point2D.Double(loc.col + oddRowShift(loc.row) / 2.0, loc.row)
}
