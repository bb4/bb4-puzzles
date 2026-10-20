// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.{HexTile, PathColor}

object LoopTiles {

  /** @return true if every tile has a path of the color. Every tile of a loop of that color needs one. */
  def allHavePrimaryPath(tiles: Seq[HexTile], color: PathColor): Boolean =
    tiles.forall(tile => (0 until HexGrid.NUM_SIDES).count(side => tile.edgeColors(side) == color) == 2)
}

/**
  * What the solver needs to know about the tiles that make up the loop. The tiles are identified by their position
  * in the list, where the first one is the tile that is already placed.
  * @param tiles the tiles of the loop, starting with the one that is already placed
  * @param primaryColor the color of the loop
  */
class LoopTiles(tiles: IndexedSeq[HexTile], primaryColor: PathColor) {

  val count: Int = tiles.size
  private val primarySides: Array[Array[Int]] = tiles.map(primarySidesOf).toArray
  private val kinds: Array[Int] = primarySides.map(sides => kindOfSides(sides)).toArray
  private val colorOrdinals: Array[Array[Int]] = tiles.map(tile => Array.tabulate(HexGrid.NUM_SIDES)(tile.edgeColors(_).ordinal)).toArray

  def tile(index: Int): HexTile = tiles(index)
  def kind(index: Int): Int = kinds(index)

  /** @param which 0 or 1, to choose one of the two sides of the unrotated tile that the primary path leaves by */
  def primarySide(index: Int, which: Int): Int = primarySides(index)(which)

  /** @return how many of the tiles after the first are of the given kind. */
  def numberOfKind(kind: Int): Int = (1 until count).count(kinds(_) == kind)

  /** @return true if the primary path of the rotated tile leaves by exactly the two sides given. */
  def fits(index: Int, rotation: Int, side1: Int, side2: Int): Boolean = {
    val first = rotatedSide(index, 0, rotation)
    val second = rotatedSide(index, 1, rotation)
    (first == side1 && second == side2) || (first == side2 && second == side1)
  }

  def rotatedSide(index: Int, which: Int, rotation: Int): Int = (primarySides(index)(which) + rotation) % HexGrid.NUM_SIDES

  /** @return the colors of the six sides of the rotated tile, with three bits for each. */
  def packedColors(index: Int, rotation: Int): Int =
    (0 until HexGrid.NUM_SIDES).foldLeft(0)((packed, side) => packed | colorOnSide(index, side, rotation) << (3 * side))

  private def colorOnSide(index: Int, side: Int, rotation: Int): Int =
    colorOrdinals(index)(Math.floorMod(side - rotation, HexGrid.NUM_SIDES))

  private def primarySidesOf(tile: HexTile): Array[Int] = {
    val sides = (0 until HexGrid.NUM_SIDES).filter(side => tile.edgeColors(side) == primaryColor).toArray
    require(sides.length == 2, s"Tile $tile does not have a path of color $primaryColor")
    sides
  }

  private def kindOfSides(sides: Array[Int]): Int =
    LoopKind.kindOf(LoopKind.sidesBetween(sides(0), sides(1)))
}
