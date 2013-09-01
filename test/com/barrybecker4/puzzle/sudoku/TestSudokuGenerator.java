/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.sudoku.model.board.Board;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Barry Becker
 */
public class TestSudokuGenerator {

    /** instance under test. */
    SudokuGenerator generator;

    @Before
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void testGenerateInitialSolution2() {
        Board board = generateInitialSolution(2);

        Board expBoard = new Board(new int[][] {
            {4, 1,    3, 2},
            {2, 3,    4, 1},
            {1, 4,    2, 3},
            {3, 2,    1, 4}
        });

        assertEquals("Unexpected generated board", expBoard, board);
    }

    @Test
    public void testGenerateInitialSolution3() {
        Board board = generateInitialSolution(3);

        Board expBoard = new Board(new int[][] {
             {4, 1, 3,  9, 5, 6,  2, 8, 7},
             {6, 9, 2,  8, 7, 4,  3, 5, 1},
             {7, 5, 8,  3, 2, 1,  9, 6, 4},
             {9, 8, 5,  2, 6, 7,  1, 4, 3},
             {3, 6, 1,  4, 9, 5,  8, 7, 2},
             {2, 4, 7,  1, 8, 3,  5, 9, 6},
             {1, 2, 9,  7, 4, 8,  6, 3, 5},
             {5, 3, 4,  6, 1, 9,  7, 2, 8},
             {8, 7, 6,  5, 3, 2,  4, 1, 9}
        });

        assertEquals("Unexpected generated board", expBoard, board);
    }

    /** works only half the time!    */
    @Test
    public void testGenerateInitialSolution4Many() {

        //List<Boolean> passed = new ArrayList<>();

        for (int i=0; i < 10; i++)  {
            MathUtil.RANDOM.setSeed(i);
            Board board = generateInitialSolution(4);
            System.out.println(board);
            //passed.add(board != null);
            assertNotNull("Could not create a consistent board", board);
        }
    }

    @Test
    public void testGenerateInitialSolution4() {

        MathUtil.RANDOM.setSeed(0);
        Board board = generateInitialSolution(4);

        assertNotNull("Could not create a consistent board", board);
    }

    @Test
    public void testGeneratePuzzle2() {
        Board board = generatePuzzle(2);

        Board expBoard = new Board(new int[][] {
            {0, 1,    3, 0},
            {2, 0,    0, 0},
            {0, 0,    0, 0},
            {0, 0,    1, 0}
        });

        assertEquals("Unexpected generated board", expBoard, board);
    }

    public Board generateInitialSolution(int baseSize) {
        generator = new SudokuGenerator(baseSize);
        long start = System.currentTimeMillis();

        Board b = new Board(baseSize);
        boolean solved = generator.generateSolution(b);
        System.out.println("SOLVED = " + solved + "  Time to generate solution for size=" + baseSize
                +" was "+ (System.currentTimeMillis() - start));
        //assertTrue("The board was not solved!", solved);
        if (!solved) return null;
        return b;
    }

    public Board generatePuzzle(int baseSize) {
        generator = new SudokuGenerator(baseSize, null);
        long start = System.currentTimeMillis();
        Board b = generator.generatePuzzleBoard();
        System.out.println(" Time to generate size="+baseSize +" was "+ (System.currentTimeMillis() - start));
        return b;
    }
}
