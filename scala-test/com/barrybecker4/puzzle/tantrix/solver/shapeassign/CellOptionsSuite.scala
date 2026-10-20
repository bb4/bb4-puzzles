// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import org.scalatest.funsuite.AnyFunSuite

class CellOptionsSuite extends AnyFunSuite {

  private val color = TILES.getTile(6).primaryColor
  private val tiles = new LoopTiles(TILES.createOrderedList(6).toIndexedSeq, color)
  private val options = new CellOptions(6, tiles)

  private def optionList(set: Long): Seq[Int] = Iterator.iterate(set)(OptionSet.withoutFirst).takeWhile(_ != 0L).map(OptionSet.firstOption).toSeq

  test("the first cell has only the tile that is already placed") {
    assertResult(1L)(options.setPlacedTile(2))
    assertResult(2)(options.rotationOf(0, 0))
  }

  test("every option has the primary color on the two sides that the loop enters and leaves by") {
    for (entry <- 0 until 6; exit <- 0 until 6 if entry != exit; option <- optionList(options.optionsFor(1, entry, exit))) {
      assertResult(color.ordinal)(options.colorOnSide(1, option, entry))
      assertResult(color.ordinal)(options.colorOnSide(1, option, exit))
    }
  }

  test("only tiles of the right kind are options, and the first tile is never one") {
    for (entry <- 0 until 6; exit <- 0 until 6 if entry != exit; option <- optionList(options.optionsFor(1, entry, exit))) {
      val kind = LoopKind.kindOf(LoopKind.sidesBetween(entry, exit))
      assertResult(kind)(tiles.kind(OptionSet.tileOf(option)))
      assert(OptionSet.tileOf(option) > 0)
    }
  }

  test("a straight tile has two options and a curved tile has one") {
    for (tile <- 1 until tiles.count) {
      val straight = optionList(options.optionsFor(1, 0, 3)).count(OptionSet.tileOf(_) == tile)
      assertResult(if (tiles.kind(tile) == LoopKind.STRAIGHT) 2 else 0)(straight)
    }
    assert(optionList(options.optionsFor(1, 0, 1)).forall(option => tiles.kind(OptionSet.tileOf(option)) == LoopKind.TIGHT_CURVE))
  }
}
