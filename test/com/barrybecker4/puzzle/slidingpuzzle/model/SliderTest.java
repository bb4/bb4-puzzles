// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model;


import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.math.MathUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class SliderTest {

    @Before
    public void setUp() {
        // this makes sure the random shuffle is repeatable
        MathUtil.RANDOM.setSeed(1);
    }

    @Test
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

    @Test
    public void testBoardConstruction() {
        SliderBoard board = new SliderBoard(3);
        assertEquals("Unexpected board size", 3, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(2, 2), board.getEmptyLocation());
    }

    @Test
    public void testMediumBoardConstruction() {
        SliderBoard board = new SliderBoard(4);
        assertEquals("Unexpected board size", 4, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(3, 3), board.getEmptyLocation());
    }

    @Test
    public void testLargeBoardConstruction() {
        SliderBoard board = new SliderBoard(5);
        assertEquals("Unexpected board size", 5, board.getSize());
        assertEquals("Unexpected empty location", new ByteLocation(4, 4), board.getEmptyLocation());
    }

    @Test
    public void testBoardEquals() {
        SliderBoard board1 = new SliderBoard(4);
        SliderBoard board2 = new SliderBoard(board1);
        SliderBoard board3 = new SliderBoard(4, true);
        assertTrue(board1.equals(board2) && board2.equals(board1));
        assertTrue(board1.hashCode() == board2.hashCode());

        assertFalse(board1.equals(board3));
        assertFalse(board1.hashCode() == board3.hashCode());
    }

    @Test
    public void testBoardHash() {
        Set<SliderBoard> boards = new HashSet<>();
        SliderBoard board1 = new SliderBoard(4);
        SliderBoard board2 = new SliderBoard(board1);
        SliderBoard board3 = new SliderBoard(4, true);
        SliderBoard board4 = new SliderBoard(4, true);

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
