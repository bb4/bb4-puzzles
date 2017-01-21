// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle.model

import com.barrybecker4.puzzle.redpuzzle.model.Nub
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

/**
  * @author Barry Becker
  */
class NubSuite extends FunSuite with BeforeAndAfter {

  /** instance under test */
  private val nub = null

  @Test def testConstruction() {
    assertEquals("Unexpected num suit.", Nub.CLUB, Nub.INNY_CLUB.suit)
    assertTrue("Unexpected nub state.", Nub.OUTY_DIAMOND.isOuty)
    assertFalse("Unexpected nub state.", Nub.INNY_DIAMOND.isOuty)
  }

  @Test def testFits() {
    assertTrue(Nub.INNY_CLUB.fitsWith(Nub.OUTY_CLUB))
    assertTrue(Nub.INNY_HEART.fitsWith(Nub.OUTY_HEART))
    assertTrue(Nub.INNY_SPADE.fitsWith(Nub.OUTY_SPADE))
    assertTrue(Nub.OUTY_DIAMOND.fitsWith(Nub.INNY_DIAMOND))
  }

  @Test def testDoesNotFit() {
    assertFalse(Nub.INNY_CLUB.fitsWith(Nub.INNY_CLUB))
    assertFalse(Nub.INNY_HEART.fitsWith(Nub.OUTY_CLUB))
    assertFalse(Nub.OUTY_CLUB.fitsWith(Nub.INNY_DIAMOND))
    assertFalse(Nub.OUTY_HEART.fitsWith(Nub.OUTY_CLUB))
  }
}
