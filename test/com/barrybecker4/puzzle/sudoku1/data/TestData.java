// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.data;

import com.barrybecker4.puzzle.sudoku1.Data;

/**
 * Some sample sudoku test puzzle data.
 *
 * @author Barry Becker
 */
public class TestData {

    /** simple test of a 4*4 puzzle */
    public static final int[][] SIMPLE_4 = {
        {0, 4,    0, 0},
        {0, 0,    2, 0},
        {4, 3,    0, 0},
        {0, 0,    0, 0}
    };

    /** simple test of a 4*4 puzzle solved */
    public static final int[][] SIMPLE_4_SOLVED = {
        {2, 4,    3, 1},
        {3, 1,    2, 4},
        {4, 3,    1, 2},
        {1, 2,    4, 3}
    };


    /** simple test of a 9*9 puzzle */
    public static final int[][] SIMPLE_9 = {
        {0, 0, 9,  0, 0, 0,  0, 0, 7},
        {2, 8, 6,  4, 7, 3,  0, 0, 0},
        {0, 0, 0,  5, 9, 0,  0, 0, 0},

        {0, 2, 1,  0, 8, 0,  0, 5, 6},
        {4, 0, 0,  0, 0, 0,  0, 0, 1},
        {8, 9, 0,  0, 6, 0,  3, 4, 0},

        {0, 0, 0,  0, 5, 2,  0, 0, 0},
        {0, 0, 0,  3, 1, 6,  7, 8, 4},
        {1, 0, 0,  0, 0, 0,  6, 0, 0}
    };

    /** simple test of a 9*9 puzzle
     *(inconsistent. use only for testing)
     */
    public static final  int[][] INCONSISTENT_9 = {
        {0, 0, 3,  7, 0, 0,  0, 2, 0},
        {0, 8, 0,  9, 0, 0,  4, 0, 1},
        {0, 9, 0,  0, 2, 1,  0, 6, 3},

        {0, 5, 2,  0, 7, 0,  0, 0, 9},
        {0, 0, 6,  1, 0, 9,  7, 0, 0},
        {8, 0, 0,  0, 6, 0,  3, 1, 0},

        {5, 3, 0,  0, 4, 0,  0, 8, 2},
        {9, 0, 7,  0, 0, 3,  0, 5, 0},
        {4, 0, 0,  7, 0, 5,  1, 0, 0}
    };

    /** Complex 16x16 puzzle from grandma */
    public static final int[][] COMPLEX_16 = Data.SAMPLE_16;


    /** you should never instantiate this static class.  */
    private TestData() {}
}
