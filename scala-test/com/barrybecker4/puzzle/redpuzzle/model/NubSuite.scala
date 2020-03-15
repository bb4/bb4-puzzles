// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import org.junit.Assert.{assertEquals, assertFalse, assertTrue}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

/**
  * @author Barry Becker
  */
class NubSuite extends AnyFunSuite with BeforeAndAfter {

  /** instance under test */
  private val nub = null

  test("Construction") {
    assertResult(Nub.INNY_CLUB.suit) { Nub.CLUB }
    assert(Nub.OUTY_DIAMOND.isOuty)
    assert(!Nub.INNY_DIAMOND.isOuty)
  }

  test("Fits") {
    assert(Nub.INNY_CLUB.fitsWith(Nub.OUTY_CLUB))
    assert(Nub.INNY_HEART.fitsWith(Nub.OUTY_HEART))
    assert(Nub.INNY_SPADE.fitsWith(Nub.OUTY_SPADE))
    assert(Nub.OUTY_DIAMOND.fitsWith(Nub.INNY_DIAMOND))
  }

  test("DoesNotFit") {
    assert(!Nub.INNY_CLUB.fitsWith(Nub.INNY_CLUB))
    assert(!Nub.INNY_HEART.fitsWith(Nub.OUTY_CLUB))
    assert(!Nub.OUTY_CLUB.fitsWith(Nub.INNY_DIAMOND))
    assert(!Nub.OUTY_HEART.fitsWith(Nub.OUTY_CLUB))
  }

  test("nub serialization") {
    assertResult("outy Suit(S)") {Nub.OUTY_SPADE.toString}
  }
}
