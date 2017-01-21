// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.redpuzzle.model.{Nub, PieceList, PieceLists}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

/**
  * @author Barry Becker
  */
class PieceListSuite extends FunSuite with BeforeAndAfter {
  /** instance under test */
  private var pieceList: PieceList = _

  @Test def testConstructionOfEmptyList() {
    pieceList = new PieceList
    assertEquals("Unexpected size.", 0, pieceList.size)
  }

  @Test def testConstruction() {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertEquals("Unexpected size.", 4, pieceList.size)
    assertTrue("Piece should have been there.", pieceList.contains(PieceLists.INITIAL_PIECES_4(1)))
    assertEquals("Unexpected number of fits.", 1, pieceList.getNumFits(0))
    assertEquals("Unexpected number of fits.", 0, pieceList.getNumFits(1))
    assertEquals("Unexpected number of fits.", 1, pieceList.getNumFits(2))
    assertEquals("Unexpected number of fits.", 0, pieceList.getNumFits(3))
  }

  @Test def testFits() {
    pieceList = new PieceList(PieceLists.INITIAL_PIECES_4)
    assertEquals("Unexpected number of fits before rotating.", 0, pieceList.getNumFits(1))
    // after rotating there should be a fit
    val newPieceList = pieceList.rotate(1, 1)
    assertEquals("Unexpected number of fits after rotating.", 1, newPieceList.getNumFits(1))
  }
}
