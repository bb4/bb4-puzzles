// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.IntLocation;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.THREE_TILES;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class HexUtilTest {

    private static final double TOL = 0.001;

    Tantrix tantrix;

    @Test
    public void testGetValidNeighborLocation() {
        tantrix = new TantrixBoard(THREE_TILES).getTantrix();

        assertEquals("Unexpected neighbor location.",
                new IntLocation(21, 22), HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 0));
        assertEquals("Unexpected neighbor location.",
                new IntLocation(20, 21), HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 1));
        assertEquals("Unexpected neighbor location.",
                new IntLocation(20, 20), HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 2));
        assertEquals("Unexpected neighbor location.",
                new IntLocation(21, 20), HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 3));
        assertEquals("Unexpected neighbor location.",
                new IntLocation(22, 20), HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 4));
        assertEquals("Unexpected neighbor location.",
                new IntLocation(22, 21), HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 5));
    }


    @Test(expected = AssertionError.class)
    public void testGetInvalidNeighborLocation() {
        tantrix = new TantrixBoard(THREE_TILES).getTantrix();

        assertEquals("Unexpected neighbor location.",
                new IntLocation(21, 22), HexUtil.getNeighborLocation(TantrixBoard.INITIAL_LOCATION, 6));
    }

    @Test
    public void testGetDistanceBetweenDiag() {

        assertEquals("Unexpected distance.",
                1.8027, HexUtil.distanceBetween(new IntLocation(19, 21), new IntLocation(20, 22)), TOL);
    }

    @Test
    public void testGetDistanceBetweenSameRow() {

        assertEquals("Unexpected distance.",
                1.0, HexUtil.distanceBetween(new IntLocation(19, 21), new IntLocation(19, 22)), TOL);
    }

    @Test
    public void testGetDistanceBetweenSameCol() {

        assertEquals("Unexpected distance.",
                2.0, HexUtil.distanceBetween(new IntLocation(19, 20), new IntLocation(19, 22)), TOL);
    }

    @Test
    public void testGetDistanceBetweenSameSpace() {

        assertEquals("Unexpected distance.",
                0.0, HexUtil.distanceBetween(new IntLocation(20, 21), new IntLocation(20, 21)), TOL);
    }
}