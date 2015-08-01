package com.barrybecker4.puzzle.slidingpuzzle.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Barry Becker
 */
public class MoveGeneratorTest {
    private MoveGenerator generator;
    private SliderBoard board;

    private int[][] SPACE_TOP_LEFT = new int[][] {
            {0, 2, 3},
            {4, 5, 6},
            {7, 1, 8}
    };

    private int[][] SPACE_TOP_MIDDLE = new int[][] {
            {2, 0, 3},
            {4, 5, 6},
            {7, 1, 8}
    };

    private int[][] SPACE_BOTTOM_RIGHT = new int[][] {
            {1, 5, 3},
            {4, 2, 6},
            {7, 8 ,0}
    };

    private int[][] SPACE_IN_MIDDLE = new int[][] {
            {1, 2, 3},
            {4, 0, 6},
            {7, 5, 8}
    };

    @Before
    public void setUp() {
        generator = new MoveGenerator();
    }


    @Test
    public void testFindNeighborsTopLeft() {
        board = new SliderBoard(SPACE_TOP_LEFT);
        List<SlideMove> neighbors = generator.generateMoves(board);
        assertEquals("Unexpected num neighbors for top left", 2, neighbors.size());
    }

    @Test
    public void testFindNeighborsTopMiddle() {
        board = new SliderBoard(SPACE_TOP_MIDDLE);
        List<SlideMove> neighbors = generator.generateMoves(board);
        assertEquals("Unexpected num neighbors for top left", 3, neighbors.size());
    }

    @Test
    public void testFindNeighborsBottomRight() {
        board = new SliderBoard(SPACE_BOTTOM_RIGHT);
        List<SlideMove> neighbors = generator.generateMoves(board);
        assertEquals("Unexpected num neighbors for top left", 2, neighbors.size());
    }

    @Test
    public void testFindNeighborsInMiddle() {
        board = new SliderBoard(SPACE_IN_MIDDLE);
        List<SlideMove> neighbors = generator.generateMoves(board);
        assertEquals("Unexpected num neighbors for top left", 4, neighbors.size());
    }


    @Test
    public void testFindTopLeftNeighbors() {
        board = new SliderBoard(SPACE_TOP_LEFT);
        List<SlideMove> neighbors = generator.generateMoves(board);
        assertEquals("Unexpected neighbors for top left",
                "[from (row=1, column=0) to (row=0, column=0), " +
                "from (row=0, column=1) to (row=0, column=0)]",
                neighbors.toString());

    }

}
