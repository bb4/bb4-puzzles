// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.sudoku1.data.TestData;
import com.barrybecker4.puzzle.sudoku1.model.board.Board;
import com.barrybecker4.puzzle.sudoku1.model.board.Candidates;
import com.barrybecker4.puzzle.sudoku1.model.board.ValuesList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Barry Becker
 */
public class TestBoard  {

    /** instance under test */
    private Board board;


    @Before
    public void setUp() {
        MathUtil.RANDOM.setSeed(1);
    }

    @Test
    public void testBoardConstruction() {
        Board board = new Board(3);

        Board expectedBoard = new Board(new int[][] {
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0}
        });
        assertEquals("Unexpected board constructed", expectedBoard, board);
    }

    @Test
    public void testFindCellCandidatesForAll() {

        board = new Board(TestData.SIMPLE_4);
        Candidates[][] expCands = {
                {new Candidates(1, 2, 3), null,                      new Candidates(1, 3),   new Candidates(1, 3)},
                {new Candidates(1, 3),    new Candidates(1),         null,                   new Candidates(1, 3, 4)},
                {null,                    null,                      new Candidates(1),        new Candidates(1, 2)},
                {new Candidates(1, 2),    new Candidates(1, 2),      new Candidates(1, 3, 4),   new Candidates(1, 2, 3, 4)}
        };

        boolean valid = true;
        for (int i=0; i<board.getEdgeLength(); i++) {
            for (int j=0; j<board.getEdgeLength(); j++) {
                Candidates cands = board.getCell(i, j).getCandidates();
                if (expCands[i][j] != cands) valid = false;
            }
        }
        if (!valid) {
            System.out.println("baord = " + board);
        }
        for (int i=0; i<board.getEdgeLength(); i++) {
            for (int j=0; j<board.getEdgeLength(); j++) {
                Candidates cands = board.getCell(i, j).getCandidates();
                assertEquals( "Did find correct candidates for cell row=" + i + " j="+ j,
                    expCands[i][j], cands);
            }
        }
    }

    @Test
    public void testFindShuffledCellCandidates2() {

        board = new Board(TestData.SIMPLE_4);

        ValuesList cands = ValuesList.getShuffledCandidates(board.getCell(0).getCandidates());
        checkCandidates(Arrays.asList(2, 3, 1), cands);
    }

    @Test
    public void testFindShuffledCellCandidates3() {

        board = new Board(TestData.SIMPLE_9);

        ValuesList cands = ValuesList.getShuffledCandidates(board.getCell(0).getCandidates());
        checkCandidates(Arrays.asList(3, 5), cands);

        cands = ValuesList.getShuffledCandidates(board.getCell(1).getCandidates());
        checkCandidates(Arrays.asList(4, 5, 3, 1), cands);

        cands = ValuesList.getShuffledCandidates(board.getCell(2).getCandidates());
        List<Integer> expList = Collections.emptyList();
        checkCandidates(expList, cands);
    }

    private void checkCandidates(List<Integer> expCands, ValuesList actCands) {
        assertEquals("Did find correct candidates",
                expCands, actCands);
    }

    @Test
    public void testNotSolved() {
        board = new Board(TestData.SIMPLE_4);
        assertFalse("Unexpectedly solved", board.solved());
    }

    @Test
    public void testSolved() {
        board = new Board(TestData.SIMPLE_4_SOLVED);
        assertTrue("Unexpectedly not solved", board.solved());
    }

}
