/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.model;


import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.math.MathUtil;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Barry Becker
 */
public class TestSlider extends TestCase {


    @Override
    public void setUp() {
        // this makes sure the random shuffle is repeatable
        MathUtil.RANDOM.setSeed(1);
    }

    public void testRandom() {
        assertEquals("unexpected first rnd ", 985, MathUtil.RANDOM.nextInt(1000));
        assertEquals("unexpected second rnd ", 588, MathUtil.RANDOM.nextInt(1000));

        List<Integer> nums = new ArrayList<>();
        List<Integer> expList = Arrays.asList(5, 0, 4, 3, 9, 2, 8, 1, 6, 7);
        for (int i=0; i<10; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums, MathUtil.RANDOM);
        assertEquals("lists not equal", expList, nums);
    }

    public void testBoardConstruction() {
        Slider board = new Slider(3);
        assertEquals("Unexpected board size", 3, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(0, 2), board.getEmptyLocation());
    }

    public void testMediumBoardConstruction() {
        Slider board = new Slider(4);
        assertEquals("Unexpected board size", 4, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(0, 3), board.getEmptyLocation());
    }

    public void testLargeBoardConstruction() {
        Slider board = new Slider(5);
        assertEquals("Unexpected board size", 5, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(0, 2), board.getEmptyLocation());
    }

    public void testBoardEquals() {
        Slider board1 = new Slider(3);
        Slider board2 = new Slider(board1);
        Slider board3 = new Slider(3);
        assertTrue(board1.equals(board2) && board2.equals(board1));
        assertTrue(board1.hashCode() == board2.hashCode());

        assertFalse(board1.equals(board3));
        assertFalse(board1.hashCode() == board3.hashCode());
    }

    public void testBoardHash() {
        Set<Slider> boards = new HashSet<Slider>();
        Slider board1 = new Slider(3);
        Slider board2 = new Slider(board1);
        Slider board3 = new Slider(3);
        Slider board4 = new Slider(3);

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
