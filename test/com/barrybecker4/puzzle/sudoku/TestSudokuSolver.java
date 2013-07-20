/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.sudoku.data.TestData;
import com.barrybecker4.puzzle.sudoku.model.board.Board;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Barry Becker
 */
public class TestSudokuSolver extends TestCase {

    /** instance under test. */
    SudokuSolver solver;

    SudokuGenerator generator;

    /**
     * common initialization for all go test cases.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MathUtil.RANDOM.setSeed(0);
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    public void testCaseSimpleSample() {

        solver = new SudokuSolver();
        boolean solved = solver.solvePuzzle(new Board(TestData.SIMPLE_9));
        Assert.assertTrue( "Did not solve SIMPLE_9 successfully", solved);
    }

    /** negative test case */
    public void testImpossiblePuzzle() {

        solver = new SudokuSolver();
        boolean solved = solver.solvePuzzle(new Board(TestData.INCONSISTENT_9));

        Assert.assertFalse( "Solved impossible SIMPLE_9 puzzle. Should not have.", solved);
    }


    /** negative test case */
    public void testSolving16x16Puzzle() {

        solver = new SudokuSolver();
        boolean solved = solver.solvePuzzle(new Board(TestData.COMPLEX_16));

        Assert.assertTrue("Unexpected could not solve 16x16 puzzle.", solved);
    }


    public void testGenerateAndSolve2() {

            generateAndSolve(2);
    }

    public void testGenerateAndSolve3() {

            generateAndSolve(3);
    }

    public void testGenerateLotsAndSolveMany() {

        for (int r=0; r < 40; r++)
            MathUtil.RANDOM.setSeed(r);
            generateAndSolve(3);

    }

    /** The large tests takes a long time because of the exponential growth with the size of the puzzle. */
    public void testGenerateAndSolve() {
        // super exponential run time
        generateAndSolve(2);  // 16  cells       32 ms
        generateAndSolve(3);  // 81  cells      265 ms
        // generateAndSolve(4);  // 256 cells    2,077 ms
        // generateAndSolve(5);  // 625 cells  687,600 ms    too slow
    }


    public void generateAndSolve(int baseSize) {
        Board board = generatePuzzle(baseSize);
        solve(board);
    }

    public Board generatePuzzle(int baseSize) {
        generator = new SudokuGenerator(baseSize, null);
        long start = System.currentTimeMillis();
        Board b = generator.generatePuzzleBoard();
        System.out.println("Time to generate size="+baseSize +" was "+ (System.currentTimeMillis() - start));
        return b;
    }

    public void solve(Board board) {
        SudokuSolver solver = new SudokuSolver();
        long start = System.currentTimeMillis();
        boolean solved = solver.solvePuzzle(board);
        System.out.println("Time to solve was "+ (System.currentTimeMillis() - start));
        Assert.assertTrue("Unexpectedly not solved.", solved);
    }
}
