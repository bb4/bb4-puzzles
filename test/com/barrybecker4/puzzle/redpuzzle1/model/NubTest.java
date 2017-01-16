package com.barrybecker4.puzzle.redpuzzle1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Barry Becker
 */
public class NubTest {

    /** instance under test */
    private Nub nub;

    @Test
    public void testConstruction() {

        assertEquals("Unexpected num suit.", Nub.Suit.CLUB, Nub.INNY_CLUB.getSuit());
        assertTrue("Unexpected nub state.", Nub.OUTY_DIAMOND.isOuty());
        assertFalse("Unexpected nub state.", Nub.INNY_DIAMOND.isOuty());
    }

    @Test
    public void testFits() {

        assertTrue(Nub.INNY_CLUB.fitsWith(Nub.OUTY_CLUB));
        assertTrue(Nub.INNY_HEART.fitsWith(Nub.OUTY_HEART));
        assertTrue(Nub.INNY_SPADE.fitsWith(Nub.OUTY_SPADE));
        assertTrue(Nub.OUTY_DIAMOND.fitsWith(Nub.INNY_DIAMOND));
    }

    @Test
    public void testDoesNotFit() {

        assertFalse(Nub.INNY_CLUB.fitsWith(Nub.INNY_CLUB));
        assertFalse(Nub.INNY_HEART.fitsWith(Nub.OUTY_CLUB));
        assertFalse(Nub.OUTY_CLUB.fitsWith(Nub.INNY_DIAMOND));
        assertFalse(Nub.OUTY_HEART.fitsWith(Nub.OUTY_CLUB));
    }

}
