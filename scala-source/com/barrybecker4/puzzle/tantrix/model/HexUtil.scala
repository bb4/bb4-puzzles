// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import java.awt.geom.Point2D

import com.barrybecker4.common.geometry.{IntLocation, Location}

/**
  * Used to find neighboring locations in hex space.
  * Tiles are arranged like this:
  * 0,0   0,1   0,2
  * 1,0   1,1   1,2   1,3
  * 2,0   2,1   2,2
  * @author Barry Becker
  */
object HexUtil {

  /** Odd rows are shifted back one.
    * @param loc       source location
    * @param direction side to navigate to to find the neighbor. 0 is to the right.
    * @return the indicated neighbor of the specified tile.
    */
  def getNeighborLocation(loc: Location, direction: Int): Location = {
    val row = loc.getRow
    val col = loc.getCol
    val colOffset = if (Math.abs(row) % 2 == 1) -1
    else 0

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
  def distanceBetween(loc1: Location, loc2: Location): Double = {
    val row1 = loc1.getRow
    val row2 = loc2.getRow
    val point1 = new Point2D.Double(loc1.getCol + (if (row1 % 2 == 1) -0.5
    else 0), row1)
    val point2 = new Point2D.Double(loc2.getCol + (if (row2 % 2 == 1) -0.5
    else 0), row2)
    point1.distance(point2)
  }
}
