// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path;

import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import junit.framework.TestCase;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*;

/**
 * @author Barry Becker
 */
public class PathEvaluatorTest extends TestCase {

    /** within this tolerance is acceptable */
    private static final double TOL = 0.0001;

    /** instance under test */
    private PathEvaluator evaluator = new PathEvaluator();


    public void testEvaluateLoopPathWith3Tiles() {
        verifyFitness(place3SolvedTiles(), 4.257);
    }

    public void testEvaluateNonLoopPathWith2Tiles_1_2() {
        verifyFitness(place2of3Tiles_OneThenTwo(), 1.2857);
    }

    public void testEvaluateNonLoopPathWith2Tiles_1_3() {
        verifyFitness(place2of3Tiles_OneThenThree(), 1.2857);
    }

    // high score because all paths (even secondary) match
    public void testEvaluateNonLoopPathWith3Tiles() {
        verifyFitness(place3UnsolvedTiles(), 1.26025);
    }

    public void testEvaluateNonLoopPathWith1Tile() {
        verifyFitness(place1of3Tiles_startingWithTile2(), 0.0);
    }


    private void verifyFitness(TantrixBoard board, double expectedFitness) {
        TantrixPath path = new TantrixPath(board.getTantrix(), board.getPrimaryColor());
        assertEquals("Unexpected fitness",
                expectedFitness, evaluator.evaluateFitness(path), TOL);
    }
}