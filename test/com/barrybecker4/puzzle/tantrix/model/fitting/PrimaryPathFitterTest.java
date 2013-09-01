// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model.fitting;

import com.barrybecker4.puzzle.tantrix.model.HexTiles;
import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.Rotation;
import com.barrybecker4.puzzle.tantrix.model.Tantrix;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import org.junit.Test;

import java.util.List;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class PrimaryPathFitterTest  {

    /** instance under test */
    PrimaryPathFitter fitter;

    Tantrix tantrix;
    HexTiles tiles = new HexTiles();

    /**
     * Here we ask if there are fits where three should be possible.
     *        1
     *    (3)   2
     */
    @Test
    public void testFitOnTwoWhereOnePossible() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements = fitter.getFittingPlacements(tiles.getTile(3), loc(2, 0));
        assertEquals("Unexpected fitting Placements. placements=\n"+placements, 1, placements.size());
    }

    /**
     * Here we ask if there are fits where three should be possible.
     *        1
     *    (4)   2
     */
    @Test
    public void testFitOnTwoWhereNonePossible() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements = fitter.getFittingPlacements(tiles.getTile(4), loc(2, 0));
        assertEquals("Unexpected fitting Placements. placements=\n"+placements, 0, placements.size());
    }

    /**
     * Here we ask if there are fits at a location where none are possible,  but
     * we consider fits where no primary paths touch, hence 3 is expected.
     *     1   (3)
     *       2
     */
    @Test
    public void testFitOnTwoWhereNoPrimaryMatchPossible() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements = fitter.getFittingPlacements(tiles.getTile(3), loc(1, 2));
        System.out.println("tantrix="+ tantrix.values());
        System.out.println("  placements=" +  placements);
        assertEquals("Unexpected fitting Placements.", 3, placements.size());
    }

    /**
     * Its not possible to have any primary path fits on a completed loop, but
     * we consider fits where no primary paths touch, hence 3 is expected
     *     (4)    1
     *         3    2
     */
    @Test
    public void testFitOnThreeLoop() {

        tantrix = place3SolvedTiles().getTantrix();
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        List<TilePlacement> placements =
                fitter.getFittingPlacements(tiles.getTile(4), loc(1, 0));
        assertEquals("Unexpected fitting Placements.", 2, placements.size());
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
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_0);
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        assertTrue("Unexpectedly did not fit.", fitter.isFit(tile2));
    }

    @Test
    public void testPlacementDoesNotFit60() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_60);
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        assertFalse("Unexpectedly fit.", fitter.isFit(tile2));
    }

    @Test
    public void testPlacementFits() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();

        TilePlacement tile2 = new TilePlacement(TILES.getTile(2), loc(2, 0), Rotation.ANGLE_300);
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        assertTrue("Unexpectedly did not fit.", fitter.isFit(tile2));
    }

    @Test
    public void testTile2PlacementFits() {
        tantrix = place1of3Tiles_startingWithTile2().getTantrix();

        TilePlacement tile2 = new TilePlacement(TILES.getTile(3), loc(0, 0), Rotation.ANGLE_60);
        fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        System.out.println(tantrix);

        assertTrue("Unexpectedly fit.", fitter.isFit(tile2));
    }

    /* The tiles form a path but not a loop */
    @Test
    public void testNumFitsFor3UnsolvedTiles() {

        tantrix = place3UnsolvedTiles().getTantrix();
        PrimaryPathFitter fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        assertEquals("Unexpected number of fits.", 4, fitter.numPrimaryFits());
    }

    /* The tiles do not even form a path */
    @Test
    public void testNumFitsFor3NonPathTiles() {

        tantrix = place3NonPathTiles().getTantrix();
        PrimaryPathFitter fitter = new PrimaryPathFitter(tantrix, PathColor.YELLOW);
        assertEquals("Unexpected number of fits.", 2, fitter.numPrimaryFits());
    }
}