package com.barrybecker4.puzzle.slidingpuzzle1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Barry on 7/11/2015.
 */
public class SliderBoardTest {

    private SliderBoard board;

    private int[][] SOLVED_3 = new int[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
    };

    private int[][] ALMOST_SOLVED_3 = new int[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 0, 8}
    };

    private int[][] SPACE_TOP_LEFT = new int[][] {
            {0, 2, 3},
            {4, 5, 6},
            {7, 1, 8}
    };

    private int[][] RANDOM_BOARD = new int[][] {
            {3, 2, 6},
            {8, 0, 1},
            {7, 5, 4}
    };


    @Test
    public void testDimension() {
        board = new SliderBoard(ALMOST_SOLVED_3);
        assertEquals("Unexpected dimension (size)", 3, board.getSize());
    }

    @Test
    public void testEquality() {
        board = new SliderBoard(ALMOST_SOLVED_3);
        SliderBoard board2 = new SliderBoard(ALMOST_SOLVED_3);
        assertEquals("Boards unexpectedly not equal", board2, board);
    }

    @Test
    public void testInequality() {
        board = new SliderBoard(ALMOST_SOLVED_3);
        SliderBoard board2 = new SliderBoard(SPACE_TOP_LEFT);
        assertNotEquals("Boards unexpectedly equal", board2, board);
    }

    @Test
    public void testBoardIsNotSolved() {
        board = new SliderBoard(ALMOST_SOLVED_3);
        assertEquals("Unexpected hamming distance", 1, board.getHamming());
        assertFalse("Unexpectedly solved", board.isSolved());
    }

    @Test
    public void testBoardIsSolved() {
        board = new SliderBoard(SOLVED_3);
        assertEquals("Unexpected hamming distance", 0, board.getHamming());
        assertTrue("Unexpectedly not solved", board.isSolved());
    }

    @Test
     public void testOneStepDistance() {
        board = new SliderBoard(ALMOST_SOLVED_3);
        assertEquals("Unexpected hamming distance", 1, board.getHamming());
        assertEquals("Unexpected manhattan distance", 1, board.getManhattan());
    }

}
