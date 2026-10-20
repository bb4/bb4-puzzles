// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.shapeassign

import com.barrybecker4.puzzle.tantrix.model.HexTiles.TILES
import org.scalatest.funsuite.AnyFunSuite

class DomainPropagatorSuite extends AnyFunSuite {

  private val tiles = new LoopTiles(TILES.createOrderedList(6).toIndexedSeq, TILES.getTile(6).primaryColor)
  private val options = new CellOptions(6, tiles)
  private val contacts = new ContactList(10)
  private val propagator = new DomainPropagator(6, options, contacts)

  private def tilesSet(numbers: Int*): Long = numbers.map(tile => OptionSet.single(OptionSet.optionOf(tile, 0))).reduce(_ | _)

  test("a tile that is the only one left for a cell is taken away from the other cells, repeatedly") {
    val domains = Array(1L, tilesSet(1), tilesSet(1, 2), tilesSet(2, 3), 0L, 0L)
    assert(propagator.propagate(domains))
    assertResult(Seq(tilesSet(1), tilesSet(2), tilesSet(3)))(domains.slice(1, 4).toSeq)
  }

  test("two cells that only have the same tile fail") {
    assert(!propagator.propagate(Array(1L, tilesSet(1), tilesSet(1), 0L, 0L, 0L)))
  }

  test("cells that are not placed yet are left alone") {
    val domains = Array(1L, tilesSet(1), 0L, 0L, 0L, 0L)
    assert(propagator.propagate(domains))
    assertResult(0L)(domains(3))
  }

  test("after propagating, each option of a cell has an option of the cell it touches with the same color on the touching sides") {
    contacts.rollbackTo(0)
    contacts.add(1, 1, 2, 4)
    val domains = Array(1L, options.optionsFor(1, 0, 3), options.optionsFor(2, 0, 3), 0L, 0L, 0L)
    val before = domains.clone()
    assert(propagator.propagate(domains))
    assert(domains(1) != 0L && domains(2) != 0L && (domains(1) & ~before(1)) == 0L)
    def colors(cell: Int, side: Int) = Iterator.iterate(domains(cell))(OptionSet.withoutFirst).takeWhile(_ != 0L)
      .map(set => options.colorOnSide(cell, OptionSet.firstOption(set), side)).toSet
    assertResult(colors(2, 4))(colors(1, 1))
    contacts.rollbackTo(0)
  }
}
