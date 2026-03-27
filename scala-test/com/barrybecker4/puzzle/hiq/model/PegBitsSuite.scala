package com.barrybecker4.puzzle.hiq.model

import org.scalatest.funsuite.AnyFunSuite

class PegBitsSuite extends AnyFunSuite {

  test("getIndexForPosition maps 33 valid cells to distinct indices 0..32") {
    val indices = for {
      i <- 0 until PegBoard.SIZE
      j <- 0 until PegBoard.SIZE
      if PegBoard.isValidPosition(i, j)
    } yield PegBits().getIndexForPosition(i, j)
    assert(indices.distinct.length == PegBoard.NUM_PEG_HOLES)
    assert(indices.toSet == (0 until PegBoard.NUM_PEG_HOLES).toSet)
  }

  test("getIndexForPosition throws for invalid board coordinates") {
    assertThrows[IllegalArgumentException] {
      PegBits().getIndexForPosition(0, 0)
    }
  }

  test("set and get preserve other bits") {
    val b0 = PegBits()
    val b1 = b0.set(5, value = true)
    assert(!b0.get(5))
    assert(b1.get(5))
    assert(!b1.get(4))
    val b2 = b1.set(10, value = true)
    assert(b2.get(5) && b2.get(10))
    val b3 = b2.set(5, value = false)
    assert(!b3.get(5) && b3.get(10))
  }

  test("getNumPegsLeft matches naive count over indices") {
    val pb = PegBits(bits = 0x1234567, finalBit = true, nextToFinalBit = false)
    val naive = (0 until PegBoard.NUM_PEG_HOLES).count(i => pb.get(i))
    assert(pb.getNumPegsLeft == naive)
  }
}
