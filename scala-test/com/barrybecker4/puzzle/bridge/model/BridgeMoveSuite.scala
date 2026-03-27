// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import org.junit.Assert.assertEquals
import org.scalatest.funsuite.AnyFunSuite

class BridgeMoveSuite extends AnyFunSuite {

  test("costIsSlowestCrossingTime") {
    assertEquals(1, BridgeMove(List(1), direction = true).cost)
    assertEquals(8, BridgeMove(List(1, 8), direction = true).cost)
    assertEquals(5, BridgeMove(List(2, 5), direction = false).cost)
  }
}
