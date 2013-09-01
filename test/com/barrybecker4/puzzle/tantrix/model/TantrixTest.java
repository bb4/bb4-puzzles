// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3SolvedTiles;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3UnsolvedTiles;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Barry Becker
 */
public class TantrixTest {

    /** instance under test */
    Tantrix tantrix;

    @Test
    public void test3TilePlacement() {
        tantrix = place3SolvedTiles().getTantrix();

        System.out.println(tantrix);
        verifyPlacement(new ByteLocation(22, 21));
        verifyPlacement(new ByteLocation(22, 20));
        verifyPlacement(new ByteLocation(21, 21));
    }

    private void verifyPlacement(Location loc) {
        TilePlacement placement = tantrix.get(loc);
        assertNotNull("Placement at " + loc + " was unexpectedly null", placement);
        assertEquals("Unexpected tiles at " + loc,
                loc, placement.getLocation());
    }

    @Test
    public void testGetNeighborLocationOnOddRow() {
        tantrix = place3UnsolvedTiles().getTantrix();

        Location loc = new ByteLocation(1, 1);

        assertEquals("Unexpected right neighbor",
                new ByteLocation(1, 2), HexUtil.getNeighborLocation(loc, 0));
        assertEquals("Unexpected bottom left neighbor",
                new ByteLocation(2, 0), HexUtil.getNeighborLocation(loc, 4));
        assertEquals("Unexpected bottom right neighbor",
                new ByteLocation(2, 1), HexUtil.getNeighborLocation(loc, 5));
    }

    @Test
    public void testGetNeighborLocationOnEvenRow() {
        tantrix = place3UnsolvedTiles().getTantrix();

        Location loc = new ByteLocation(2, 2);
        assertEquals("Unexpected right neighbor",
                new ByteLocation(2, 3), HexUtil.getNeighborLocation(loc, 0));
        assertEquals("Unexpected bottom left neighbor",
                new ByteLocation(3, 2), HexUtil.getNeighborLocation(loc, 4));
        assertEquals("Unexpected bottom right neighbor",
                new ByteLocation(3, 3), HexUtil.getNeighborLocation(loc, 5));
    }

    @Test
    public void testGetNeighborFromUnrotatedTile() {
        tantrix = place3SolvedTiles().getTantrix();
        assertEquals("Unexpected right neighbor",
                null, tantrix.getNeighbor(tantrix.getTilePlacement(2, 2), (byte) 0));

        TilePlacement bottomLeft = tantrix.getTilePlacement(3, 1);
        assertEquals("Unexpected bottom left neighbor",
                bottomLeft, tantrix.getNeighbor(tantrix.getTilePlacement(2, 2), (byte)4));

        TilePlacement bottomRight = tantrix.getTilePlacement(3, 2);
        assertEquals("Unexpected bottom right neighbor",
                bottomRight, tantrix.getNeighbor(tantrix.getTilePlacement(2, 2), (byte) 5));
    }

    @Test
    public void testGetNeighborFromRotatedTile() {
        tantrix = place3SolvedTiles().getTantrix();
        assertEquals("Unexpected right neighbor",
                null, tantrix.getNeighbor(tantrix.getTilePlacement(3, 2), (byte) 0));

        TilePlacement topLeft = tantrix.getTilePlacement(2, 2);
        assertEquals("Unexpected top left neighbor",
                topLeft, tantrix.getNeighbor(tantrix.getTilePlacement(3, 2), (byte)2));

        TilePlacement left = tantrix.getTilePlacement(3, 1);
        assertEquals("Unexpected left neighbor",
                left, tantrix.getNeighbor(tantrix.getTilePlacement(3, 2), (byte) 3));
    }
}