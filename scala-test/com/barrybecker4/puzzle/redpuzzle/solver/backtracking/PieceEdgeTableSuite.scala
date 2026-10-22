// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.solver.backtracking

import com.barrybecker4.puzzle.redpuzzle.model.{OrientedPiece, PieceLists}
import com.barrybecker4.puzzle.redpuzzle.solver.backtracking.PieceEdgeTable.{BOTTOM, LEFT, RIGHT, ROTATIONS, SIDES, TOP}
import org.scalatest.funsuite.AnyFunSuite

class PieceEdgeTableSuite extends AnyFunSuite {

  private val pieces = PieceLists.RED_INITIAL_PIECES_9.toSeq.map(p => OrientedPiece(p))
  private val table = new PieceEdgeTable(pieces)

  test("there is a slot for every rotation of every piece") {
    assertResult(9)(table.numPieces)
    assertResult(pieces.size * ROTATIONS - 1)(table.slot(pieces.size - 1, ROTATIONS - 1))
  }

  test("a slot knows which piece it is a rotation of") {
    for (piece <- pieces.indices; rotation <- 0 until ROTATIONS)
      assertResult(piece)(table.pieceOf(table.slot(piece, rotation)))
  }

  test("the oriented piece of a slot is the piece turned by the rotation") {
    assertResult(pieces(3).rotate(2))(table.orientedPiece(table.slot(3, 2)))
  }

  test("the edge codes of every slot are those of the nubs of its oriented piece") {
    for (piece <- pieces.indices; rotation <- 0 until ROTATIONS) {
      val slot = table.slot(piece, rotation)
      val oriented = table.orientedPiece(slot)
      assertResult(NubCodes.encode(oriented.getTopNub))(table.edge(slot, TOP))
      assertResult(NubCodes.encode(oriented.getRightNub))(table.edge(slot, RIGHT))
      assertResult(NubCodes.encode(oriented.getBottomNub))(table.edge(slot, BOTTOM))
      assertResult(NubCodes.encode(oriented.getLeftNub))(table.edge(slot, LEFT))
    }
  }

  test("turning a piece a quarter turn moves each nub to the next side") {
    for (side <- 0 until SIDES)
      assertResult(table.edge(table.slot(0, 0), (side + 1) % SIDES))(table.edge(table.slot(0, 1), side))
  }
}
