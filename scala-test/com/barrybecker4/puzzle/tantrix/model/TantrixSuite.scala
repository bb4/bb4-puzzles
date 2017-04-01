// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.common.geometry.{Box, ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import org.junit.Assert.{assertEquals, assertNotNull}
import org.scalatest.FunSuite

/**
  * @author Barry Becker
  */
class TantrixSuite extends FunSuite {

  var tantrix: Tantrix = _

  test("test3TilePlacement") {
    tantrix = place3SolvedTiles.tantrix
    //System.out.println(tantrix)
    verifyPlacement(new ByteLocation(22, 21))
    verifyPlacement(new ByteLocation(22, 20))
    verifyPlacement(new ByteLocation(21, 21))
  }

  test("GetNeighborLocationOnOddRow") {
    tantrix = place3UnsolvedTiles.tantrix
    val loc = new ByteLocation(1, 1)
    assertEquals("Unexpected right neighbor", new ByteLocation(1, 2), HexUtil.getNeighborLocation(loc, 0))
    assertEquals("Unexpected bottom left neighbor", new ByteLocation(2, 0), HexUtil.getNeighborLocation(loc, 4))
    assertEquals("Unexpected bottom right neighbor", new ByteLocation(2, 1), HexUtil.getNeighborLocation(loc, 5))
  }

  test("GetNeighborLocationOnEvenRow") {
    tantrix = place3UnsolvedTiles.tantrix
    val loc = new ByteLocation(2, 2)
    assertEquals("Unexpected right neighbor", new ByteLocation(2, 3), HexUtil.getNeighborLocation(loc, 0))
    assertEquals("Unexpected bottom left neighbor", new ByteLocation(3, 2), HexUtil.getNeighborLocation(loc, 4))
    assertEquals("Unexpected bottom right neighbor", new ByteLocation(3, 3), HexUtil.getNeighborLocation(loc, 5))
  }

  test("GetNeighborFromUnrotatedTile") {
    tantrix = place3SolvedTiles.tantrix
    assertEquals("Unexpected right neighbor", None, tantrix.getNeighbor(tantrix(21, 21).get, 0))
    val bottomLeft = tantrix(22, 20)
    assertEquals("Unexpected bottom left neighbor", bottomLeft, tantrix.getNeighbor(tantrix(21, 21).get, 4))
    val bottomRight = tantrix(22, 21)
    assertEquals("Unexpected bottom right neighbor", bottomRight, tantrix.getNeighbor(tantrix(21, 21).get, 5))
  }

  test("GetNeighborFromRotatedTile") {
    tantrix = place3SolvedTiles.tantrix
    assertEquals("Unexpected right neighbor", None, tantrix.getNeighbor(tantrix(22, 21).get, 0))
    val topLeft = tantrix(21, 21)
    assertEquals("Unexpected top left neighbor", topLeft, tantrix.getNeighbor(tantrix(22, 21).get, 2))
    val left = tantrix(22, 20)
    assertEquals("Unexpected left neighbor", left, tantrix.getNeighbor(tantrix(22, 21).get, 3))
  }

  test("Bounding box for 3 solved tiles") {
    tantrix = place3SolvedTiles.tantrix
    assertResult(new Box(new ByteLocation(21, 20), new ByteLocation(22, 21))) { tantrix.getBoundingBox }
  }

  test("Bounding box for 6 unsolved tiles") {
    tantrix = place6UnsolvedTiles.tantrix
    assertResult(new Box(new ByteLocation(19, 20), new ByteLocation(21, 22))) { tantrix.getBoundingBox }
  }


  private def verifyPlacement(loc: Location) {
    val placement = tantrix(loc)
    assertNotNull("Placement at " + loc + " was unexpectedly null", placement)
    assertEquals("Unexpected tiles at " + loc, loc, placement.get.location)
  }
}