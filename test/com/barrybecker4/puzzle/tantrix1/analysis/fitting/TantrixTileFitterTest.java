// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.analysis.fitting;

import com.barrybecker4.puzzle.tantrix1.TantrixTstUtil;
import com.barrybecker4.puzzle.tantrix1.model.*;
import org.junit.Test;

import java.util.List;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.*;
import static org.junit.Assert.*;

/**
 * @author Barry Becker
 */
public class TantrixTileFitterTest {

    private static final HexTiles TILES = new HexTiles();

    /** instance under test */
    private TantrixTileFitter fitter;

    private Tantrix tantrix;


    /**
     * Here we ask if there are fits where three should be possible.
     *        1
     *    (3)   2
     */
    @Test
    public void testFitOnTwoWhereOnePossible() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements = fitter.getFittingPlacements(TILES.getTile(3), loc(2, 0));
        assertEquals("Unexpected fitting Placements. placements=\n" + placements, 1, placements.size());
    }

    /**
     * Here we ask if there are fits where three should be possible.
     *        1
     *    (4)   2
     */
    @Test
    public void testFitOnTwoWhereNonePossible() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements = fitter.getFittingPlacements(TILES.getTile(4), loc(2, 0));
        assertEquals("Unexpected fitting Placements. placements=\n" + placements, 0, placements.size());
    }

    /**
     * Here we ask if there are fits at a location where no primary path connections are possible.
     *     1   (3)
     *       2
     */
    @Test
    public void testFitOnTwoWhereNoPrimaryMatchPossible() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements = fitter.getFittingPlacements(TILES.getTile(3), loc(1, 2));
        assertEquals("Unexpected fitting Placements.", 0, placements.size());
    }

    /**
     * Its not possible to have any primary path fits on a completed loop.
     *     (4)    1
     *         3    2
     */
    @Test
    public void testFitOnThreeLoop() {

        tantrix = place3SolvedTiles().getTantrix();
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements =
                fitter.getFittingPlacements(TILES.getTile(4), loc(1, 0));
        assertEquals("Unexpected fitting Placements.", 0, placements.size());
    }

    /**
     * In this case it will fit because we are only checking that one primary path matches,
     * but if we were being strict and using TileFitter it would not.
     *    1
     * (2)  3
     */
    @Test
    public void testPlacementDoesNotFit0() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        System.out.println("tantrix="+tantrix);
        TilePlacement tile2 = new TilePlacement(TantrixTstUtil.TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0);
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        assertTrue("Unexpectedly did not fit.", fitter.isFit(tile2));
    }

    @Test
    public void testPlacementDoesNotFit60() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        TilePlacement tile2 = new TilePlacement(TantrixTstUtil.TILES.getTile(2), loc(2, 0), Rotation.ANGLE_60);
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        assertFalse("Unexpectedly fit.", fitter.isFit(tile2));
    }

    @Test
    public void testPlacementFits() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();

        TilePlacement tile2 = new TilePlacement(TantrixTstUtil.TILES.getTile(2), loc(2, 0), Rotation.ANGLE_300);
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        assertTrue("Unexpectedly did not fit.", fitter.isFit(tile2));
    }

    @Test
    public void testTile2PlacementFits() {
        tantrix = place1of3Tiles_startingWithTile2().getTantrix();

        TilePlacement tile2 = new TilePlacement(TantrixTstUtil.TILES.getTile(3), loc(0, 0), Rotation.ANGLE_60);
        fitter = new TantrixTileFitter(tantrix, PathColor.YELLOW);
        System.out.println(tantrix);

        assertTrue("Unexpectedly fit.", fitter.isFit(tile2));
    }

}