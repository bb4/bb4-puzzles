// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix

import com.barrybecker4.puzzle.tantrix.generation.MoveGenerator
import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard
import com.barrybecker4.puzzle.tantrix.TantrixTstUtil._
import org.scalatest.funsuite.AnyFunSuite

class TantrixControllerSuite extends AnyFunSuite {

  private val ui = new StubTantrixRefreshable

  test("isGoal true for fully solved small boards") {
    val c = new TantrixController(ui)
    assert(c.isGoal(place3SolvedTiles))
    assert(c.isGoal(place4SolvedTiles))
    assert(c.isGoal(place7SolvedTiles))
  }

  test("isGoal false for partial or inconsistent boards") {
    val c = new TantrixController(ui)
    assert(!c.isGoal(place3UnsolvedTiles))
    assert(!c.isGoal(place4UnsolvedTiles))
    assert(!c.isGoal(place7LoopWrongColorTiles))
    assert(!c.isGoal(place10LoopWithInnerSpace))
  }

  test("transition appends placement consistently") {
    val c = new TantrixController(ui)
    val b0 = place2of3Tiles_OneThenTwo
    val move = new MoveGenerator(b0).generateMoves.head
    val b1 = c.transition(b0, move)
    assert(b1.unplacedTiles.size == b0.unplacedTiles.size - 1)
    assert(b1.tantrix.size == b0.tantrix.size + 1)
  }

  test("distanceFromGoal uses path fitness unsolved greater than solved") {
    val c = new TantrixController(ui)
    val dSolved = c.distanceFromGoal(place3SolvedTiles)
    val dPartial = c.distanceFromGoal(place3of6UnsolvedTiles)
    val dAnother = c.distanceFromGoal(place3UnsolvedTiles)
    assert(dSolved == 0)
    assert(dPartial > dSolved)
    assert(dAnother > dSolved)
  }

  test("legalTransitions nonempty for typical in-progress boards") {
    val c = new TantrixController(ui)
    assert(new com.barrybecker4.puzzle.tantrix.generation.MoveGenerator(place2of3Tiles_OneThenTwo).generateMoves.nonEmpty)
    assert(c.legalTransitions(place2of3Tiles_OneThenTwo).nonEmpty)
    assert(c.legalTransitions(new TantrixBoard(TILES.createOrderedList(3))).nonEmpty)
  }
}
