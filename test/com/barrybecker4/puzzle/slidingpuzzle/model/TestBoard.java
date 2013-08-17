/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.model;


import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.math.MathUtil;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Barry Becker Date: Jul 3, 2006
 */
public class TestBoard extends TestCase {

    /** instance under test */
    Board board;


    @Override
    public void setUp() {
        // this makes sure the random shuffle is repeatable
        MathUtil.RANDOM.setSeed(1);
    }

    public void testBoardConstruction() {
        Board board = new Board(3);
        assertEquals("Unexpected board size", 3, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(2, 1), board.getEmptyLocation());
    }

    public void testLargeBoardConstruction() {
        Board board = new Board(5);
        assertEquals("Unexpected board size", 5, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(2, 2), board.getEmptyLocation());
    }

    public void testBoardEquals() {
        Board board1 = new Board(3);
        Board board2 = new Board(board1);
        Board board3 = new Board(3);
        assertTrue(board1.equals(board2) && board2.equals(board1));
        assertTrue(board1.hashCode() == board2.hashCode());

        assertFalse(board1.equals(board3));
        assertFalse(board1.hashCode() == board3.hashCode());
    }

    public void testBoardHash() {

        Set<Board> boards = new HashSet<Board>();
        Board board1 = new Board(3);
        Board board2 = new Board(board1);
        Board board3 = new Board(3);
        Board board4 = new Board(3);

        boards.add(board1);
        boards.add(board2);
        boards.add(board3);

        assertEquals("Unexpected number boards in set", 2, boards.size());
        assertTrue(boards.contains(board1));
        assertTrue(boards.contains(board2));
        assertTrue(boards.contains(board3));
        assertFalse(boards.contains(board4));
    }

}
