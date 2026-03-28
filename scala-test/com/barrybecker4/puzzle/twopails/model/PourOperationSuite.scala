// Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model

import com.barrybecker4.puzzle.twopails.model.PourOperation.Action.*
import com.barrybecker4.puzzle.twopails.model.PourOperation.Container.*
import org.scalatest.funsuite.AnyFunSuite

class PourOperationSuite extends AnyFunSuite {

  test("reverse is involution for FILL and EMPTY") {
    val fillFirst = PourOperation(FILL, FIRST)
    assert(fillFirst.reverse.reverse == fillFirst)
    val emptySecond = PourOperation(EMPTY, SECOND)
    assert(emptySecond.reverse.reverse == emptySecond)
  }

  test("reverse swaps FILL and EMPTY on same container") {
    assert(PourOperation(FILL, FIRST).reverse == PourOperation(EMPTY, FIRST))
    assert(PourOperation(EMPTY, SECOND).reverse == PourOperation(FILL, SECOND))
  }

  test("reverse swaps containers for TRANSFER only") {
    val t1 = PourOperation(TRANSFER, FIRST)
    assert(t1.reverse == PourOperation(TRANSFER, SECOND))
    val t2 = PourOperation(TRANSFER, SECOND)
    assert(t2.reverse == PourOperation(TRANSFER, FIRST))
    assert(t1.reverse.reverse == t1)
  }
}
