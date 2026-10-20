// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.{IntLocation, Location}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable

/**
  * Tests the number of steps between hex locations.
  */
class HexDistanceSuite extends AnyFunSuite {

  private def loc(row: Int, col: Int): Location = IntLocation(row, col)

  test("the distance from a location to itself is zero") {
    assertResult(0)(HexUtil.hexDistance(loc(21, 21), loc(21, 21)))
  }

  test("every neighbor is one step away, on odd and even and negative rows") {
    for (row <- -3 to 4; col <- -2 to 2; direction <- 0 until HexTile.NUM_SIDES) {
      val start = loc(row, col)
      val neighbor = HexUtil.getNeighborLocation(start, direction)
      assertResult(1, s"$start to its neighbor $neighbor in direction $direction")(HexUtil.hexDistance(start, neighbor))
    }
  }

  test("the distance is the same as the number of neighbor steps found by a breadth first search") {
    val origin = loc(4, 3)
    val steps = mutable.Map[Location, Int](origin -> 0)
    val queue = mutable.Queue[Location](origin)
    while (queue.nonEmpty) {
      val current = queue.dequeue()
      if (steps(current) < 6) {
        for (direction <- 0 until HexTile.NUM_SIDES) {
          val neighbor = HexUtil.getNeighborLocation(current, direction)
          if (!steps.contains(neighbor)) {
            steps(neighbor) = steps(current) + 1
            queue.enqueue(neighbor)
          }
        }
      }
    }
    assert(steps.size > 100)
    for ((location, expected) <- steps) {
      assertResult(expected, s"from $origin to $location")(HexUtil.hexDistance(origin, location))
      assertResult(expected, s"from $location to $origin")(HexUtil.hexDistance(location, origin))
    }
  }

  test("same row distance is the difference in columns") {
    assertResult(3)(HexUtil.hexDistance(loc(21, 21), loc(21, 24)))
  }
}
