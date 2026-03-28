// Copyright by Barry G. Becker, 2017 - 2025. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil.*
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.Rotation.*
import com.barrybecker4.puzzle.tantrix.model.{Tantrix, TilePlacement}
import org.scalatest.funsuite.AnyFunSuite

class PathifierSuite extends AnyFunSuite {

  test("reorder returns single tile unchanged") {
    val board = place1of3Tiles_startingWithTile2
    val pathifier = Pathifier(board.primaryColor)
    assert(pathifier.reorder(board.tantrix) == Seq(board.getLastTile))
  }

  test("reorder restores yellow path order for three tiles") {
    val ordered = List(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
      TilePlacement(TILE1, UPPER, ANGLE_0),
      TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    val tantrix = new Tantrix(ordered)
    val pathifier = Pathifier(PathColor.YELLOW)
    assert(pathifier.reorder(tantrix) == ordered)
  }

  test("reorder walks seven tiles given out of storage order") {
    val tiles = sevenTilesInAWrongRedLoop
    val tantrix = new Tantrix(tiles)
    val pathifier = Pathifier(PathColor.RED)
    val ordered = pathifier.reorder(tantrix)
    assert(ordered.length == 7)
    assert(TantrixPath.hasOrderedPrimaryPath(ordered, PathColor.RED))
  }
}
