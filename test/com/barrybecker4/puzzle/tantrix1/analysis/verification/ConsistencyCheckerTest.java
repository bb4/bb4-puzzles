// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.analysis.verification;

import com.barrybecker4.puzzle.tantrix1.PathTstUtil;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.Tantrix;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;
import com.barrybecker4.puzzle.tantrix1.solver.verfication.ConsistencyChecker;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class ConsistencyCheckerTest {

    /** instance under test */
    private ConsistencyChecker checker;

    private Tantrix tantrix;

    /**
     * Check consistency of the first two placed tiles.
     */
    @Test
    public void testFitOnTwoWhereOnePossible() {

        tantrix = place2of3Tiles_OneThenTwo().getTantrix();
        checker = new ConsistencyChecker(tantrix.values(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 2, checker.numFittingTiles());
    }


    /**
     * Test consistency of loop formed by first 3 tiles.
     */
    @Test
    public void testFitOnThreeLoop() {

        tantrix = place3SolvedTiles().getTantrix();
        checker = new ConsistencyChecker(tantrix.values(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 3, checker.numFittingTiles());
    }

    /**
     * Consistency of tiles one and three.
     *    1
     * (2)  3
     */
    @Test
    public void testPlacementDoesNotFit0() {
        tantrix = place2of3Tiles_OneThenThree().getTantrix();
        checker = new ConsistencyChecker(tantrix.values(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 2, checker.numFittingTiles());
    }

    /** if only one tile placed, it is consistent but has not fits. */
    @Test
    public void testOneTilePlacementConsistency() {
        tantrix = place1of3Tiles_startingWithTile2().getTantrix();

        checker = new ConsistencyChecker(tantrix.values(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 0, checker.numFittingTiles());
    }

    /** Three loop has 3 fitting tiles. */
    @Test
    public void testLoopPath3Consistency() {
        TantrixPath path = PathTstUtil.LOOP_PATH3;

        checker = new ConsistencyChecker(path.getTilePlacements(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 3, checker.numFittingTiles());
    }

    /** Three tiles that are not in a loop. */
    @Test
    public void testPath3Consistency() {
        TantrixPath path = PathTstUtil.NON_LOOP_PATH3;

        checker = new ConsistencyChecker(path.getTilePlacements(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 1, checker.numFittingTiles());

        checker = new ConsistencyChecker(path.getTilePlacements(), PathColor.BLUE);
        assertEquals("Unexpected number of fitting tiles.", 0, checker.numFittingTiles());
    }

    /** Three loop has 3 fitting tiles. */
    @Test
    public void testLoopPath4Consistency() {
        TantrixPath path = PathTstUtil.LOOP_PATH4;

        checker = new ConsistencyChecker(path.getTilePlacements(), PathColor.RED);
        assertEquals("Unexpected number of fitting tiles.", 4, checker.numFittingTiles());

        checker = new ConsistencyChecker(path.getTilePlacements(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 0, checker.numFittingTiles());
    }

    /** Three tiles that are not in a loop. */
    @Test
    public void testPath4Consistency() {
        TantrixPath path = PathTstUtil.NON_LOOP_PATH4;

        checker = new ConsistencyChecker(path.getTilePlacements(), PathColor.RED);
        assertEquals("Unexpected number of fitting tiles.", 2, checker.numFittingTiles());

        checker = new ConsistencyChecker(path.getTilePlacements(), PathColor.YELLOW);
        assertEquals("Unexpected number of fitting tiles.", 1, checker.numFittingTiles());
    }
}