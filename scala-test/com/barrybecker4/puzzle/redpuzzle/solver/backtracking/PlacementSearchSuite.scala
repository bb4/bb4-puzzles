// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver.backtracking

import com.barrybecker4.puzzle.redpuzzle.model.{Nub, OrientedPiece, Piece, PieceList, PieceLists}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class PlacementSearchSuite extends AnyFunSuite {

  private def searchFor(pieces: PieceList, onProgress: (Array[Int], Long) => Unit = (_, _) => ()) = {
    val table = new PieceEdgeTable(pieces.pieces)
    (table, new PlacementSearch(table, pieces.edgeLength, onProgress))
  }

  private def assertFitsTogether(placed: Seq[OrientedPiece], numTotal: Int): Unit = {
    assertResult(numTotal)(placed.size)
    placed.indices.foreach(i => assert(PieceList(placed.take(i).toList, numTotal).fits(placed(i)), s"cell $i"))
  }

  test("fills every cell of the 9 piece puzzle with pieces that fit, for many shuffles") {
    for (seed <- 0 until 20) {
      val (table, search) = searchFor(PieceLists.getInitialPuzzlePieces(9, new Random(seed)))
      val slots = search.solve().getOrElse(fail(s"no solution for seed $seed"))
      assertFitsTogether(slots.toSeq.map(table.orientedPiece), 9)
    }
  }

  test("uses each piece exactly once") {
    val (table, search) = searchFor(PieceLists.getInitialPuzzlePieces(9, new Random(3)))
    val used = search.solve().get.map(table.pieceOf).sorted.toList
    assertResult((0 until 9).toList)(used)
  }

  test("fills the 2 by 2 puzzle") {
    val (table, search) = searchFor(PieceLists.getInitialPuzzlePieces(4, new Random(1)))
    assertFitsTogether(search.solve().get.toSeq.map(table.orientedPiece), 4)
  }

  test("finds no solution when no pieces can be next to each other") {
    val outy = Piece(Nub.OUTY_SPADE, Nub.OUTY_SPADE, Nub.OUTY_SPADE, Nub.OUTY_SPADE, _: Int)
    val (_, search) = searchFor(new PieceList((1 to 4).map(outy).toArray))
    assert(search.solve().isEmpty)
    assert(search.triesSoFar > 0)
    assertResult(0)(search.placedSoFar.length)
  }

  test("reports progress once for every 1024 placements tried") {
    var reports = 0
    var lastTries = 0L
    val (_, search) = searchFor(PieceLists.getInitialPuzzlePieces(9, new Random(5)), (_, tries) => {
      reports += 1
      lastTries = tries
    })
    search.solve()
    assertResult(search.triesSoFar / PlacementSearch.PROGRESS_INTERVAL)(reports)
    assert(lastTries % PlacementSearch.PROGRESS_INTERVAL == 0)
  }

  test("stops when the progress report throws") {
    val seed = (0 until 50).find { s =>
      val (_, probe) = searchFor(PieceLists.getInitialPuzzlePieces(9, new Random(s)))
      probe.solve()
      probe.triesSoFar > PlacementSearch.PROGRESS_INTERVAL
    }.getOrElse(fail("expected some shuffle to need more tries than the progress interval"))
    val (_, search) = searchFor(PieceLists.getInitialPuzzlePieces(9, new Random(seed)),
      (_, _) => throw new InterruptedException("cancelled"))
    intercept[InterruptedException](search.solve())
    assertResult(PlacementSearch.PROGRESS_INTERVAL.toLong)(search.triesSoFar)
  }
}
