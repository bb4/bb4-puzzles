// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.analysis.verification;

import com.barrybecker4.puzzle.tantrix1.TantrixTstUtil;
import com.barrybecker4.puzzle.tantrix1.model.Tantrix;
import com.barrybecker4.puzzle.tantrix1.solver.verfication.InnerSpaceDetector;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class InnerSpaceDetectorTest {

    /** instance under test */
    private InnerSpaceDetector detector;

    private Tantrix tantrix;

    /**
     * Two tiles do not have a loop
     */
    @Test
    public void testThatTwoTilesDoNotHaveInnerSpace() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        verifyHasInnerSpaces(false);
    }

    /**
     * The first three tiles are a yellow loop.
     */
    @Test
    public void testThreeLoopDoesNotHaveInnerSpace() {

        tantrix = place3SolvedTiles().getTantrix();
        verifyHasInnerSpaces(false);
    }

    @Test
    public void testPlacementDoesNotHaveInnerSpace() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        verifyHasInnerSpaces(false);
    }

    /** one tile cannot be a loop. */
    @Test
    public void testOneTileNoInnerSpace() {
        tantrix = place1of3Tiles_startingWithTile2().getTantrix();
        verifyHasInnerSpaces(false);
    }

    /** This is a loop, but it has inner space. */
    @Test
    public void test10TileLoopWithInnerSpace() {
        tantrix = place10LoopWithInnerSpace().getTantrix();
        verifyHasInnerSpaces(true);
    }

    /** This is a loop, but it has inner space. */
    @Test
    public void testJumbled9() {
        tantrix = TantrixTstUtil.placeJumbled9().getTantrix();
        verifyHasInnerSpaces(false);
    }

    /**
     * @param expHasInnerSpace true if we expect one or more inner spaces.
     */
    private void verifyHasInnerSpaces(boolean expHasInnerSpace) {
        detector = new InnerSpaceDetector(tantrix);
        if (expHasInnerSpace) {
            assertTrue("Unexpectedly did not have inner spaces.", detector.hasInnerSpaces());
        }
        else {
            assertFalse("Unexpectedly had inner spaces.", detector.hasInnerSpaces());
        }
    }
}