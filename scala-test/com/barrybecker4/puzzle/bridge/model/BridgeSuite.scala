// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model

import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.scalatest.funsuite.AnyFunSuite

class BridgeSuite extends AnyFunSuite {

  test("applyFromPeopleArray") {
    val b = Bridge(Array(1, 2, 5, 8))
    assertEquals(List(1, 2, 5, 8), b.uncrossed)
    assertEquals(Nil, b.crossed)
    assertFalse(b.lightCrossed)
  }

  test("isSolvedWhenAllCrossed") {
    assertTrue(Bridge(Nil, List(1, 2), lightCrossed = true).isSolved)
    assertFalse(Bridge(List(1), Nil, lightCrossed = false).isSolved)
  }

  test("applyMoveForwardThenReverseRestoresState") {
    val start = Bridge(Array(1, 2, 5, 8))
    val move = BridgeMove(List(1, 2), direction = true)
    val afterForward = start.applyMove(move, reverse = false)
    assertEquals(List(5, 8), afterForward.uncrossed)
    assertEquals(List(1, 2), afterForward.crossed)
    assertTrue(afterForward.lightCrossed)
    val back = afterForward.applyMove(move, reverse = true)
    assertEquals(start.uncrossed.sorted, back.uncrossed.sorted)
    assertEquals(start.crossed.sorted, back.crossed.sorted)
    assertEquals(start.lightCrossed, back.lightCrossed)
  }

  test("distanceFromGoalSumsUncrossedSpeeds") {
    val b = Bridge(List(1, 2, 5), Nil, lightCrossed = false)
    assertEquals(8, b.distanceFromGoal)
  }
}
