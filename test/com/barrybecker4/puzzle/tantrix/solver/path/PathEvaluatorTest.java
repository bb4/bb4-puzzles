// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver.path;

import com.barrybecker4.puzzle.tantrix.model.PathColor;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import org.junit.Test;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class PathEvaluatorTest {

    /** within this tolerance is acceptable */
    private static final double TOL = 0.0001;

    /** instance under test */
    private PathEvaluator evaluator = new PathEvaluator();

    @Test
    public void testEvaluateLoopPathWith3Tiles() {
        verifyFitness(place3SolvedTiles(), 0);
    }

    @Test
    public void testEvaluateNonLoopPathWith2Tiles_1_2() {
        verifyFitness(place2of3Tiles_OneThenTwo(), 1.8142857142857145);
    }

    @Test
    public void testEvaluateNonLoopPathWith2Tiles_1_3() {
        verifyFitness(place2of3Tiles_OneThenThree(), 1.8142857142857145);
    }

    /** high score because all paths (even secondary) match  */
    @Test
    public void testEvaluateNonLoopPathWith3Tiles() {
        verifyFitness(place3UnsolvedTiles(), 1.8397849462365594);
    }

    @Test
    public void testEvaluateNonLoopPathWith1Tile() {
        verifyFitness(place1of3Tiles_startingWithTile2(), 3.1);
    }

    @Test
    public void testEvaluate4UnsolvedTiles() {
        verifyFitness(place4UnsolvedTiles(), 1.9036585365853658);
    }

    @Test
    public void testEvaluate4SolvedTiles() {
        verifyFitness(place4SolvedTiles(), 0);
    }

    @Test
    public void testEvaluate10LoopWithInnerSpace() {
        verifyFitness(place10LoopWithInnerSpace(), 0.8229702970297034, PathColor.RED);
    }

    @Test
    public void testEvaluate9AlmostLoop() {
        verifyFitness(place9AlmostLoop(), 1.7960622005113114, PathColor.RED);
    }

    @Test(expected = IllegalStateException.class)
    public void testEvaluateJumbled9() {
        verifyFitness(placeJumbled9(), 1.303938, PathColor.RED);
    }

    private void verifyFitness(TantrixBoard board, double expectedFitness) {
        verifyFitness(board, expectedFitness, board.getPrimaryColor());
    }


    private void verifyFitness(TantrixBoard board, double expectedFitness, PathColor color) {
        TantrixPath path = new TantrixPath(board.getTantrix(), color);
        assertEquals("Unexpected fitness",
                expectedFitness, evaluator.evaluateFitness(path), TOL);
    }
}