// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.Location;
import junit.framework.TestCase;

import java.util.Set;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*;

/**
 * @author Barry Becker
 */
public class BorderFinderTest extends TestCase {

    /** instance under test */
    BorderFinder borderFinder;
    Tantrix tantrix;


    public void testFindBorderForFirstTileOfThree() {
        tantrix = new TantrixBoard(threeTiles).getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 6, positions.size());
    }

    public void testFindBorderForTwoOfThreeTilesA() {
        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 8, positions.size());
    }

    public void testFindBorderForTwoOfThreeTilesB() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);
        System.out.println(tantrix);
        Set<Location> positions = borderFinder.findBorderPositions();
        System.out.println(positions);
        assertEquals("Unexpected number of border locations.", 8, positions.size());
    }

    public void testFindBorderForThreeSolvedTiles() {
        tantrix = place3SolvedTiles().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 9, positions.size());
    }


    public void testFindBorderForTwoOfThreeTilesA_ConstrainedByBorder() {
        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW);

        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", 2, positions.size());
    }

    public void testFindBorderForTwoOfThreeTilesB_ConstrainedByBorder() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW);
        System.out.println(tantrix);
        Set<Location> positions = borderFinder.findBorderPositions();
        System.out.println(positions);
        assertEquals("Unexpected number of border locations.", 2, positions.size());
    }
}