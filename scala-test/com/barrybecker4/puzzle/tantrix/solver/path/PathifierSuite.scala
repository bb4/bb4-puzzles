// Copyright by Barry G. Becker, 2017 - 2025. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path

import com.barrybecker4.puzzle.tantrix.PathTstUtil.*
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*
import com.barrybecker4.puzzle.tantrix.model.PathColor
import com.barrybecker4.puzzle.tantrix.model.Rotation.*
import com.barrybecker4.puzzle.tantrix.model.{Tantrix, TilePlacement}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class PathifierSuite extends AnyFunSuite {

  private def assertSameTileSet(before: Tantrix, ordered: Seq[TilePlacement]): Unit = {
    val locsBefore = before.tiles.map(_.location).toSet
    assert(ordered.map(_.location).toSet == locsBefore)
    assert(ordered.size == before.size)
  }

  private def assertValidPath(color: PathColor, ordered: Seq[TilePlacement]): Unit = {
    assert(TantrixPath.hasOrderedPrimaryPath(ordered, color),
      s"Expected valid $color primary path, got:\n${ordered.mkString(", ")}")
  }

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

  test("Pathifier require rejects null primary color") {
    assertThrows[IllegalArgumentException] {
      Pathifier(null)
    }
  }

  test("reorder two-tile open primary path") {
    val board = place2of3Tiles_OneThenTwo
    val pathifier = Pathifier(board.primaryColor)
    val ordered = pathifier.reorder(board.tantrix)
    assert(ordered.size == 2)
    assertSameTileSet(board.tantrix, ordered)
    assertValidPath(board.primaryColor, ordered)
  }

  test("reorder place3SolvedTiles yellow path") {
    val board = place3SolvedTiles
    val pathifier = Pathifier(board.primaryColor)
    val ordered = pathifier.reorder(board.tantrix)
    assert(ordered.size == 3)
    assertSameTileSet(board.tantrix, ordered)
    assertValidPath(board.primaryColor, ordered)
  }

  test("reorder place4SolvedTiles primary path") {
    val board = place4SolvedTiles
    val pathifier = Pathifier(board.primaryColor)
    val ordered = pathifier.reorder(board.tantrix)
    assert(ordered.size == 4)
    assertSameTileSet(board.tantrix, ordered)
    assertValidPath(board.primaryColor, ordered)
  }

  test("reorder place7SolvedTiles blue loop") {
    val board = place7SolvedTiles
    val pathifier = Pathifier(board.primaryColor)
    val ordered = pathifier.reorder(board.tantrix)
    assert(ordered.size == 7)
    assertSameTileSet(board.tantrix, ordered)
    assertValidPath(board.primaryColor, ordered)
  }

  test("reorder partial six-tile board three placed") {
    val board = place3of6UnsolvedTiles
    val pathifier = Pathifier(board.primaryColor)
    val ordered = pathifier.reorder(board.tantrix)
    assert(ordered.size == 3)
    assertSameTileSet(board.tantrix, ordered)
    assertValidPath(board.primaryColor, ordered)
  }

  test("reorder seven tile blue loop resilient to deterministic shuffle of input seq") {
    val pathifier = Pathifier(PathColor.BLUE)
    for (seed <- Seq(0, 1, 42, 99)) {
      val shuffled = Random(seed).shuffle(sevenTilesInABlueLoop.toSeq)
      val tantrix = new Tantrix(shuffled)
      val ordered = pathifier.reorder(tantrix)
      assert(ordered.length == 7)
      assertSameTileSet(tantrix, ordered)
      assertValidPath(PathColor.BLUE, ordered)
    }
  }

  test("reorder four tile red loop from shuffled PathTstUtil path") {
    val pathifier = Pathifier(PathColor.RED)
    for (seed <- Seq(0, 7, 13)) {
      val shuffled = Random(seed).shuffle(LOOP_PATH4.tiles)
      val tantrix = new Tantrix(shuffled)
      val ordered = pathifier.reorder(tantrix)
      assert(ordered.length == 4)
      assertSameTileSet(tantrix, ordered)
      assertValidPath(PathColor.RED, ordered)
    }
  }

  test("reorder idempotent on explicitly ordered list as constructor seq") {
    val ordered = List(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
      TilePlacement(TILE1, UPPER, ANGLE_0),
      TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    val pathifier = Pathifier(PathColor.YELLOW)
    val first = pathifier.reorder(new Tantrix(ordered))
    val second = pathifier.reorder(new Tantrix(first))
    assert(first == ordered)
    assert(second == ordered)
  }

  test("reorder place3NonPathTiles covers all tiles Pathifier completes") {
    val board = place3NonPathTiles
    val pathifier = Pathifier(board.primaryColor)
    val ordered = pathifier.reorder(board.tantrix)
    assert(ordered.size == 3)
    assertSameTileSet(board.tantrix, ordered)
    assert(!board.isSolved)
  }

  test("reorder place6UnsolvedTiles covers all six tiles") {
    val board = place6UnsolvedTiles
    val pathifier = Pathifier(board.primaryColor)
    val ordered = pathifier.reorder(board.tantrix)
    assert(ordered.size == 6)
    assertSameTileSet(board.tantrix, ordered)
  }

  test("reorder throws when wrong path color for blue loop fixture") {
    val tantrix = new Tantrix(sevenTilesInABlueLoop)
    val pathifier = Pathifier(PathColor.RED)
    assertThrows[IllegalStateException] {
      pathifier.reorder(tantrix)
    }
  }

  test("reorder reversed constructor order yields valid yellow path three tiles") {
    val canonical = List(
      TilePlacement(TILE2, LOWER_RIGHT, ANGLE_60),
      TilePlacement(TILE1, UPPER, ANGLE_0),
      TilePlacement(TILE3, LOWER_LEFT, ANGLE_120))
    val reversed = canonical.reverse
    val pathifier = Pathifier(PathColor.YELLOW)
    val out = pathifier.reorder(new Tantrix(reversed))
    assert(out.size == 3)
    assert(out.toSet == canonical.toSet)
    assertValidPath(PathColor.YELLOW, out)
  }

  test("reorder medium path LOOP_PATH7 blue after shuffle") {
    val pathifier = Pathifier(PathColor.BLUE)
    for (seed <- Seq(0, 2, 17, 33)) {
      val shuffled = Random(seed).shuffle(LOOP_PATH7.tiles)
      val tantrix = new Tantrix(shuffled)
      val ordered = pathifier.reorder(tantrix)
      assert(ordered.length == 7)
      assertSameTileSet(tantrix, ordered)
      assertValidPath(PathColor.BLUE, ordered)
    }
  }
}
