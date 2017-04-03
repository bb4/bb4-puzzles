// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.analysis.verification;

import com.barrybecker4.puzzle.tantrix1.PathTstUtil;
import com.barrybecker4.puzzle.tantrix1.model.PathColor;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.solver.path.TantrixPath;
import com.barrybecker4.puzzle.tantrix1.solver.verfication.LoopDetector;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix1.TantrixTstUtil.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class LoopDetectorTest {

    /** instance under test */
    private LoopDetector detector;

    private TantrixBoard board;

    /**
     * Two tiles do not have a loop
     */
    @Test
    public void testThatTwoTilesDoNotHaveLoop() {

        board = place2of3Tiles_OneThenTwo();
        verifyHasLoop(false);
    }

    /**
     * The first three tiles are a yellow loop.
     */
    @Test
    public void testFitOnThreeLoop() {

        board = place3SolvedTiles();
        verifyHasLoop(true);
    }

    @Test
    public void testPlacementDoesNotFit0() {
        board = place2of3Tiles_OneThenThree();
        verifyHasLoop(false);
    }

    /** one tile cannot be a loop. */
    @Test
    public void testOneTileNotALoop() {
        board = place1of3Tiles_startingWithTile2();
        verifyHasLoop(false);
    }

    /** 10 tiles that are in a loop, but have inner space. Still a loop though. */
    @Test
    public void testLoopPath10LoopWithSpace() {
        board = place10LoopWithInnerSpace();
        verifyHasLoop(true);
    }

    /** Three loop has 3 fitting tiles. */
    @Test
    public void testLoopPath3HasLoop() {
        TantrixPath path = PathTstUtil.LOOP_PATH3;
        board = new TantrixBoard(path.getTilePlacements(), PathColor.YELLOW);
        verifyHasLoop(true);

        // though there is a yellow loop, there should not be a blue loop
        board = new TantrixBoard(path.getTilePlacements(), PathColor.BLUE);
        verifyHasLoop(false);
    }

    /** Three tiles that are not in a loop. */
    @Test
    public void testPath3WithNoLoop() {
        TantrixPath path = PathTstUtil.NON_LOOP_PATH3;
        board = new TantrixBoard(path.getTilePlacements(), PathColor.YELLOW);
        verifyHasLoop(false);
    }

    /** Three loop has 3 fitting tiles. */
    @Test
    public void testLoopPath4HasLoop() {
        TantrixPath path = PathTstUtil.LOOP_PATH4;

        board = new TantrixBoard(path.getTilePlacements(), PathColor.RED);
        verifyHasLoop(true);
    }

    /** Three tiles that are not in a loop. */
    @Test
    public void testLoopPath4NoLoop() {
        TantrixPath path = PathTstUtil.NON_LOOP_PATH4;

        board = new TantrixBoard(path.getTilePlacements(), PathColor.RED);
        verifyHasLoop(false);
    }

    /**
     * @param expHasLoop true if we expect a loop.
     */
    private void verifyHasLoop(boolean expHasLoop) {
        detector = new LoopDetector(board);
        if (expHasLoop) {
            assertTrue("Unexpectedly did not have loop.", detector.hasLoop());
        }
        else {
            assertFalse("Unexpectedly had loop.", detector.hasLoop());
        }
    }
}