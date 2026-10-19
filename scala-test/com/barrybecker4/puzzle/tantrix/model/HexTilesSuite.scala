// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import org.scalatest.funsuite.AnyFunSuite

/**
  * The tiles were entered by hand, so it is worth verifying that they are all consistent with the rules of Tantrix.
  * Every other part of the puzzle (like finding the two outgoing paths of the primary color) relies on this.
  */
class HexTilesSuite extends AnyFunSuite {

  private val NUM_TILES = 56
  private val allTiles: Seq[HexTile] = TILES.createOrderedList(NUM_TILES)

  private def edgeColors(tile: HexTile): Seq[PathColor] =
    (0 until HexTile.NUM_SIDES).map(side => tile.edgeColors(side))

  /** @return a representation of the tile edge colors that is the same no matter how the tile is rotated. */
  private def rotationIndependentColors(tile: HexTile): String = {
    val colors = edgeColors(tile).map(_.ordinal)
    (0 until HexTile.NUM_SIDES).map(shift => (colors.drop(shift) ++ colors.take(shift)).mkString(",")).min
  }

  test("there are 56 tiles, numbered consecutively starting at 1") {
    assertResult(NUM_TILES) { allTiles.size }
    assertResult((1 to NUM_TILES).toList) { allTiles.map(_.tantrixNumber.toInt).toList }
  }

  test("getTile returns the tile with the specified number") {
    for (n <- 1 to NUM_TILES)
      assertResult(n, s"tile $n") { TILES.getTile(n).tantrixNumber.toInt }
  }

  test("every tile has three path colors, and each of them is on exactly two of the six edges") {
    for (tile <- allTiles) {
      val edgesPerColor = edgeColors(tile).groupBy(identity).map { case (color, edges) => color -> edges.size }
      assertResult(3, s"number of path colors on tile ${tile.tantrixNumber}") { edgesPerColor.size }
      assert(edgesPerColor.values.forall(_ == 2), s"tile ${tile.tantrixNumber} has edges $edgesPerColor")
    }
  }

  test("tiles that can be used in the puzzle have two paths of their primary color") {
    for (n <- 1 to TILES.getMaxSolitaireSize) {
      val tile = TILES.getTile(n)
      val outgoing = TilePlacement(tile, TantrixBoard.INITIAL_LOCATION, Rotation.ANGLE_0)
        .getOutgoingPathLocations(tile.primaryColor)
      assertResult(2, s"number of primary paths on tile $n") { outgoing.size }
    }
  }

  test("tiles with a white back do not have any white paths") {
    for (tile <- allTiles.filter(_.primaryColor == PathColor.WHITE))
      assert(!edgeColors(tile).contains(PathColor.WHITE), s"tile ${tile.tantrixNumber}")
    assertResult(true) { allTiles.exists(_.primaryColor == PathColor.WHITE) }
  }

  test("no two tiles are the same, even if one is rotated") {
    assertResult(NUM_TILES) { allTiles.map(rotationIndependentColors).toSet.size }
  }

  test("createOrderedList returns the first tiles in numerical order") {
    assertResult(allTiles.take(7)) { TILES.createOrderedList(7) }
    assertResult(Seq.empty) { TILES.createOrderedList(0) }
  }

  test("createRandomList returns the same tiles that createOrderedList does, but in any order") {
    val randomTiles = TILES.createRandomList(10)
    assertResult(10) { randomTiles.size }
    assertResult(TILES.createOrderedList(10).toSet) { randomTiles.toSet }
  }

  test("different HexTiles instances share the same tiles so that tiles from them are equal") {
    val other = new HexTiles()
    for (n <- 1 to NUM_TILES)
      assert(other.getTile(n) == TILES.getTile(n), s"tile $n")
    assertResult(TILES.createOrderedList(5)) { other.createOrderedList(5) }
  }
}
