/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku.model;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.sudoku.data.TestData;
import com.barrybecker4.puzzle.sudoku.model.board.Board;
import com.barrybecker4.puzzle.sudoku.model.board.Candidates;
import com.barrybecker4.puzzle.sudoku.model.board.Cell;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Barry Becker
 */
public class TestCell extends TestCase {

    /** instance under test */
    Cell cell;
    Board board;

     @Override
    public void setUp() {
        MathUtil.RANDOM.setSeed(1);
    }

    public void testFindCellCandidatesForFirstCell() {

        board = new Board(TestData.SIMPLE_4);
        cell = board.getCell(0, 0);

        Candidates expCands = new Candidates(1, 2, 3); // everything but 4.
        Assert.assertEquals("Did find correct candidates",
                expCands, cell.getCandidates());
    }

    public void testFindCellCandidatesForMiddleCell() {

        board = new Board(TestData.SIMPLE_4);
        cell = board.getCell(1, 1);

        Candidates expCands = new Candidates(1);
        Assert.assertEquals("Did find correct candidates",
                expCands, cell.getCandidates());
    }

    /** Set an appropriate legal value */
    public void testSetValueValid() {
        board = new Board(TestData.SIMPLE_4);
        cell = board.getCell(1, 1);

        Assert.assertEquals("Unexpected before candidates", new Candidates(1), cell.getCandidates());

        System.out.println("before" + board);
        cell.setValue(1);
        System.out.println("after" + board);

        // the candidate lists should be reduced.
        Assert.assertEquals("Unexpected value ", 1, cell.getValue());
        Assert.assertNull(cell.getCandidates());
        Assert.assertEquals("Unexpected row 1 cands", new Candidates(3, 4), board.getRowCells().get(1).getCandidates());
        Assert.assertEquals("Unexpected col 1 cands", new Candidates(3, 4), board.getRowCells().get(1).getCandidates());
        Assert.assertEquals("Unexpected bigCell 0,0 cands", new Candidates(2, 3), board.getBigCell(0, 0).getCandidates());
    }

    /** Set an inappropriate illegal value and verify exception thrown
    public void testSetValueInvalid() {
        board = new TantrixBoard(TestData.SIMPLE_4);
        cell = board.getCell(1, 1);

        Assert.assertEquals("Unexpected before candidates", new Candidates(1), cell.getCandidates());

        System.out.println("before" + board);
        try {
            cell.setValue(3);
            Assert.fail();
        } catch (IllegalStateException e) {
            // success
        }
    }  */

    /** Calling clear on a cell should undo a set. */
    public void testClearReversesSet() {
        Board origBoard = new Board(TestData.SIMPLE_4);
        board = new Board(origBoard);

        cell = board.getCell(1, 1);

        cell.setValue(1);
        cell.clearValue();

        Assert.assertEquals("Unexpectedly not the same ", origBoard, board);
    }
}
