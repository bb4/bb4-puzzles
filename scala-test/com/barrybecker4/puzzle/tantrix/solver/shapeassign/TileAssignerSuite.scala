// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import org.scalatest.funsuite.AnyFunSuite

class TileAssignerSuite extends AnyFunSuite {

  private val tiles = new LoopTiles(TILES.createOrderedList(6).toIndexedSeq, TILES.getTile(6).primaryColor)
  private val options = new CellOptions(4, tiles)
  private val propagator = new DomainPropagator(4, options, new ContactList(4))
  private val matcher = new DistinctTileMatcher

  private def tilesSet(numbers: Int*): Long = numbers.map(tile => OptionSet.single(OptionSet.optionOf(tile, 0))).reduce(_ | _)

  test("cells that already have one option keep it") {
    val domains = Array(1L, tilesSet(1), tilesSet(2), tilesSet(3))
    assertResult(domains.toSeq)(new TileAssigner(4, propagator, matcher).assign(domains).get.toSeq)
  }

  test("cells with a choice are given different tiles") {
    val domains = Array(1L, tilesSet(1, 2), tilesSet(1, 2), tilesSet(3))
    val decided = new TileAssigner(4, propagator, matcher).assign(domains).get
    assertResult(Set(1, 2))(decided.slice(1, 3).map(set => OptionSet.tileOf(OptionSet.firstOption(set))).toSet)
    assertResult(tilesSet(3))(decided(3))
  }

  test("the domains that were passed in are not changed") {
    val domains = Array(1L, tilesSet(1, 2), tilesSet(1, 2), tilesSet(3))
    val copy = domains.clone()
    new TileAssigner(4, propagator, matcher).assign(domains)
    assertResult(copy.toSeq)(domains.toSeq)
  }

  test("no assignment is returned if the budget of options to try runs out") {
    val domains = Array(1L, tilesSet(1, 2), tilesSet(1, 2), tilesSet(3))
    assertResult(None)(new TileAssigner(4, propagator, matcher, budget = 0).assign(domains))
  }
}
