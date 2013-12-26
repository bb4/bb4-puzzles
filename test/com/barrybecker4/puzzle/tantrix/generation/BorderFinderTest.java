// Copyright by Barry G. Becker, 2012-2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.generation;

import com.barrybecker4.common.geometry.IntLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Tantrix;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*;
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
        verifyBorderLocations(new IntLocation(22, 20), new IntLocation(22, 21));
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesA() {
        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);
        verifyBorderLocations(new IntLocation(22, 20));
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesB() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);
        verifyBorderLocations(new IntLocation(22, 20));
    }

    @Test
    public void testFindBorderForThreeSolvedTiles() {
        tantrix = place3SolvedTiles().getTantrix();
        borderFinder = new BorderFinder(tantrix, 10, PathColor.YELLOW);

        verifyBorderLocations();
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesA_ConstrainedByBorder() {
        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW);
        verifyBorderLocations(new IntLocation(22, 20));
    }

    @Test
    public void testFindBorderForTwoOfThreeTilesB_ConstrainedByBorder() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        borderFinder = new BorderFinder(tantrix, 1, PathColor.YELLOW);
        verifyBorderLocations(new IntLocation(22, 20));
    }

    @Test
    public void testFindBorderFor4TilesNonLoop4Pieces() {
        tantrix = place4UnsolvedTiles().getTantrix();
        borderFinder = new BorderFinder(tantrix, 4, PathColor.RED);
        verifyBorderLocations(new IntLocation(22, 20));
    }

    @Test
    public void testFindBorderFor4TilesNonLoopFewPieces() {
        tantrix = place4UnsolvedTiles().getTantrix();
        borderFinder = new BorderFinder(tantrix, 6, PathColor.RED);
        verifyBorderLocations(new IntLocation(22, 20));
    }

    @Test
    public void testFindBorderFor4TilesNonLoopManyPieces() {
        tantrix = place4UnsolvedTiles().getTantrix();
        borderFinder = new BorderFinder(tantrix, 20, PathColor.RED);
        verifyBorderLocations(new IntLocation(22, 20));
    }

    @Test
    public void testFindBorderFor4TileLoop() {
        tantrix = place4SolvedTiles().getTantrix();
        borderFinder = new BorderFinder(tantrix, 6, PathColor.RED);
        verifyBorderLocations();
    }

    @Test
    public void testFindBorderFor10TileLoopWithInnerSpace() {
        tantrix = place10LoopWithInnerSpace().getTantrix();
        borderFinder = new BorderFinder(tantrix, 12, PathColor.RED);
        verifyBorderLocations();
    }

    @Test
    public void testFindBorderFor9TilesAlmostLoop() {
        tantrix = place9AlmostLoop().getTantrix();
        borderFinder = new BorderFinder(tantrix, 16, PathColor.RED);
        verifyBorderLocations(new IntLocation(21, 22));
    }

    private void verifyBorderLocations(IntLocation... locations) {
        Set<Location> positions = borderFinder.findBorderPositions();
        assertEquals("Unexpected number of border locations.", createSet(locations), positions);
    }

    private Set<Location> createSet(IntLocation... locations) {
        Set<Location> positions = new HashSet<>();
        Collections.addAll(positions, locations);
        return positions;
    }
}