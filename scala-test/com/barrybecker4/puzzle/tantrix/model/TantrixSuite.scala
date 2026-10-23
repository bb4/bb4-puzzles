// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import scala.compiletime.uninitialized
import com.barrybecker4.common.geometry.{Box, ByteLocation, Location}
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import org.junit.Assert.{assertEquals, assertNotNull}
import org.scalatest.funsuite.AnyFunSuite

/**
  */
class TantrixSuite extends AnyFunSuite {

  var tantrix: Tantrix = uninitialized

  test("test3TilePlacement") {
    tantrix = place3SolvedTiles.tantrix
    //System.out.println(tantrix)
    verifyPlacement(ByteLocation(22, 21))
    verifyPlacement(ByteLocation(22, 20))
    verifyPlacement(ByteLocation(21, 21))
  }

  test("GetNeighborLocationOnOddRow") {
    tantrix = place3UnsolvedTiles.tantrix
    val loc = ByteLocation(1, 1)
    assertEquals("Unexpected right neighbor", ByteLocation(1, 2), HexUtil.getNeighborLocation(loc, 0))
    assertEquals("Unexpected bottom left neighbor", ByteLocation(2, 0), HexUtil.getNeighborLocation(loc, 4))
    assertEquals("Unexpected bottom right neighbor", ByteLocation(2, 1), HexUtil.getNeighborLocation(loc, 5))
  }

  test("GetNeighborLocationOnEvenRow") {
    tantrix = place3UnsolvedTiles.tantrix
    val loc = ByteLocation(2, 2)
    assertEquals("Unexpected right neighbor", ByteLocation(2, 3), HexUtil.getNeighborLocation(loc, 0))
    assertEquals("Unexpected bottom left neighbor", ByteLocation(3, 2), HexUtil.getNeighborLocation(loc, 4))
    assertEquals("Unexpected bottom right neighbor", ByteLocation(3, 3), HexUtil.getNeighborLocation(loc, 5))
  }

  test("GetNeighborFromUnrotatedTile") {
    tantrix = place3SolvedTiles.tantrix
    assertEquals("Unexpected right neighbor", None, tantrix.getNeighbor( tantrix(21, 21), 0))
    val bottomLeft = tantrix(22, 20)
    assertEquals("Unexpected bottom left neighbor", bottomLeft, tantrix.getNeighbor(tantrix(21, 21), 4))
    val bottomRight = tantrix(22, 21)
    assertEquals("Unexpected bottom right neighbor", bottomRight, tantrix.getNeighbor(tantrix(21, 21), 5))
  }

  test("GetNeighborFromRotatedTile") {
    tantrix = place3SolvedTiles.tantrix
    assertEquals("Unexpected right neighbor", None, tantrix.getNeighbor(tantrix(22, 21), 0))
    val topLeft = tantrix(21, 21)
    assertEquals("Unexpected top left neighbor", topLeft, tantrix.getNeighbor(tantrix(22, 21), 2))
    val left = tantrix(22, 20)
    assertEquals("Unexpected left neighbor", left, tantrix.getNeighbor(tantrix(22, 21), 3))
  }

  test("Bounding box for 3 solved tiles") {
    tantrix = place3SolvedTiles.tantrix
    assertResult(new Box(ByteLocation(21, 20), ByteLocation(22, 21))) { tantrix.getBoundingBox }
  }

  test("Bounding box for 6 unsolved tiles") {
    tantrix = place6UnsolvedTiles.tantrix
    assertResult(new Box(ByteLocation(19, 20), ByteLocation(21, 22))) { tantrix.getBoundingBox }
  }


  private def verifyPlacement(loc: Location): Unit = {
    val placement = tantrix(loc)
    assertNotNull("Placement at " + loc + " was unexpectedly null", placement)
    assertEquals("Unexpected tiles at " + loc, loc, placement.get.location)
  }

  test("tantrix instances with the same placements are equal, no matter which was placed last") {
    val placements = place3SolvedTiles.tantrix.tiles.toSeq
    val oneOrder = new Tantrix(placements)
    val otherOrder = new Tantrix(placements.reverse)
    assert(oneOrder.lastTile != otherOrder.lastTile)
    assertEquals(oneOrder, otherOrder)
    assertEquals(oneOrder.hashCode, otherOrder.hashCode)
    assertEquals(Set(oneOrder), Set(otherOrder))
  }

  test("tantrix instances with different placements are not equal") {
    val placements = place3SolvedTiles.tantrix.tiles.toSeq
    val rotated = placements.head.rotate() +: placements.tail
    assert(new Tantrix(placements) != new Tantrix(rotated))
    assert(new Tantrix(placements) != new Tantrix(placements.tail))
  }

  test("boards that only differ in the order the tiles were placed are equal") {
    val board = place3SolvedTiles
    val reordered = new TantrixBoard(new Tantrix(board.tantrix.tiles.toSeq.reverse), board.primaryColor,
      board.unplacedTiles, board.numTiles)
    assertEquals(board, reordered)
    assertEquals(board.hashCode, reordered.hashCode)
  }
}

