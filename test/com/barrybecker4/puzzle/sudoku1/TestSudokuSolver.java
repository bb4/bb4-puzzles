// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.sudoku1.data.TestData;
import com.barrybecker4.puzzle.sudoku1.model.board.Board;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class TestSudokuSolver {

    /** instance under test. */
    private SudokuSolver solver;

    /**
     * common initialization for all go test cases.
     */
    @Before
    public void setUp() throws Exception {
        MathUtil.RANDOM.setSeed(0);
    }

    @Test
    public void testCaseSimpleSample() {

        solver = new SudokuSolver();
        boolean solved = solver.solvePuzzle(new Board(TestData.SIMPLE_9));
        assertTrue("Did not solve SIMPLE_9 successfully", solved);
    }

    /** negative test case */
    @Test
    public void testImpossiblePuzzle() {

        solver = new SudokuSolver();
        boolean solved = solver.solvePuzzle(new Board(TestData.INCONSISTENT_9));

        assertFalse("Solved impossible SIMPLE_9 puzzle. Should not have.", solved);
    }

    @Test
    public void testSolving16x16Puzzle() {

        solver = new SudokuSolver();
        boolean solved = solver.solvePuzzle(new Board(TestData.COMPLEX_16));


        assertTrue("Unexpected could not solve 16x16 puzzle.", solved);
    }

    @Test
    public void testGenerateAndSolve2() {
        generateAndSolve(2);
    }

    @Test
    public void testGenerateAndSolve3() {

        generateAndSolve(3);
    }

    @Test
    public void testGenerateLotsAndSolveMany() {

        for (int r=0; r < 40; r++) {
            MathUtil.RANDOM.setSeed(r);
            generateAndSolve(3);
        }
    }

    /** The large tests takes a long time because of the exponential growth with the size of the puzzle. */
    @Test
    public void testGenerateAndSolve() {
        // super exponential run time
        generateAndSolve(2);  // 16  cells       32 ms
        generateAndSolve(3);  // 81  cells      265 ms
        // generateAndSolve(4);  // 256 cells    2,077 ms
        // generateAndSolve(5);  // 625 cells  687,600 ms    too slow
    }


    private void generateAndSolve(int baseSize) {
        Board board = generatePuzzle(baseSize);
        solve(board);
    }

    private Board generatePuzzle(int baseSize) {
        SudokuGenerator generator = new SudokuGenerator(baseSize, null);
        long start = System.currentTimeMillis();
        Board b = generator.generatePuzzleBoard();
        System.out.println("Time to generate size="+baseSize +" was "+ (System.currentTimeMillis() - start));
        return b;
    }

    private void solve(Board board) {
        SudokuSolver solver = new SudokuSolver();
        long start = System.currentTimeMillis();
        boolean solved = solver.solvePuzzle(board);
        System.out.println("Time to solve was "+ (System.currentTimeMillis() - start));
        assertTrue("Unexpectedly not solved.", solved);
    }
}
