/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku.model;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.sudoku.data.TestData;
import com.barrybecker4.puzzle.sudoku.model.board.Board;
import com.barrybecker4.puzzle.sudoku.model.update.ReflectiveBoardUpdater;
import com.barrybecker4.puzzle.sudoku.model.update.updaters.LoneRangerUpdater;
import com.barrybecker4.puzzle.sudoku.model.update.updaters.StandardCRBUpdater;
import junit.framework.TestCase;

/**
 * @author Barry Becker
 */
@SuppressWarnings("unchecked")
public class TestBoardUpdater extends TestCase {

    /** instance under test */
    ReflectiveBoardUpdater updater;
    Board board;

    @Override
    public void setUp() {
        MathUtil.RANDOM.setSeed(1);
        board = new Board(TestData.SIMPLE_4);
    }


    public void testUpdateAndSetStandardCRB() {

        updater = new ReflectiveBoardUpdater(StandardCRBUpdater.class);
        updater.updateAndSet(board);

        int[][] expectedSetValues = {
            {0, 4,    0, 0},
            {0, 1,    2, 0},
            {4, 3,    1, 2},
            {0, 2,    0, 0}
        };
        verifySetValues(expectedSetValues, board);
    }


    public void testUpdateAndSetStandardCRBAndLoneRanger() {

        updater = new ReflectiveBoardUpdater(StandardCRBUpdater.class, LoneRangerUpdater.class);
        updater.updateAndSet(board);

        int[][] expectedSetValues = {
            {2, 4,    0, 1},
            {3, 1,    2, 4},
            {4, 3,    1, 2},
            {1, 2,    4, 3}
        };
        verifySetValues(expectedSetValues, board);
    }

    public void testUpdateAndSetLoneRangerAndStandardCRB() {

        updater = new ReflectiveBoardUpdater(LoneRangerUpdater.class, StandardCRBUpdater.class);
        updater.updateAndSet(board);

        int[][] expectedSetValues = {
            {2, 4,    0, 1},
            {3, 1,    2, 4},
            {4, 3,    1, 2},
            {1, 2,    4, 3}
        };
        verifySetValues(expectedSetValues, board);
    }

    public void testUpdateAndSetLoneRangerOnly() {

        updater = new ReflectiveBoardUpdater(LoneRangerUpdater.class);
        updater.updateAndSet(board);

        int[][] expectedSetValues = {
            {2, 4,    0, 0},
            {3, 1,    2, 4},
            {4, 3,    0, 2},
            {1, 2,    4, 3}
        };
        verifySetValues(expectedSetValues, board);
    }


    private void verifySetValues(int[][] expectedSetValues, Board board) {
        System.out.println("board="+ board);
        for (int i=0; i<board.getEdgeLength(); i++) {
            for (int j=0; j<board.getEdgeLength(); j++) {
                assertEquals("Unexpected set value at row="+ i +" col="+j,
                        expectedSetValues[i][j], board.getCell(i, j).getValue());
            }
        }
    }
}
