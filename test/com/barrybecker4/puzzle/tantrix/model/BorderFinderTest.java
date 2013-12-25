// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.Location;
import org.junit.Test;

import java.util.Set;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place2of3Tiles_OneThenThree;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place2of3Tiles_OneThenTwo;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3SolvedTiles;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.THREE_TILES;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class BorderFinderTest {

    /** instance under test */
    BorderFinder borderFinder;
    Tantrix tantrix;

    @Test
    public void testFindBorderForFirstTileOfThree() {
        tantrix = new TantrixBoard(THREE_TILES).getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 6, positions.size());
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesA() {
        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 8, positions.size());
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesB() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);
        System.out.println(tantrix);
        Set<Location> positions = borderFinder.findBorderPositions();
        System.out.println(positions);
        assertEquals("Unexpected number of border locations.", 8, positions.size());
    }

    @Test
    public void testFindBorderForThreeSolvedTiles() {
        tantrix = place3SolvedTiles().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 9, positions.size());
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesA_ConstrainedByBorder() {
        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 2, positions.size());
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesB_ConstrainedByBorder() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW);
        System.out.println(tantrix);
        Set<Location> positions = borderFinder.findBorderPositions();
        System.out.println(positions);
        assertEquals("Unexpected number of border locations.", 2, positions.size());
    }
}