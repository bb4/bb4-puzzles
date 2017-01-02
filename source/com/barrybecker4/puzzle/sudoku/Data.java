// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku;

/**
 * Some sample sudoku test puzzle data
 * Perhaps make these into separate date files eventually an move to test.
 *
 * @author Barry Becker
 */
public class Data {

    /** simple test of a 4*4 puzzle */
    public static final int[][] SIMPLE_4 = {
        {0, 4,    0, 0},
        {0, 0,    2, 0},
        {4, 3,    0, 0},
        {0, 0,    0, 0}
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

    /**
     * Hardest 9*9 puzzle according to
     * http://news.yahoo.com/solve-hardest-ever-sudoku-133055603--abc-news-topstories.html
     */
    public static final int[][] HARDEST_9 = {
            {8, 0, 0,  0, 0, 0,  0, 0, 0},
            {0, 0, 3,  6, 0, 0,  0, 0, 0},
            {0, 7, 0,  0, 9, 0,  2, 0, 0},

            {0, 5, 0,  0, 0, 7,  0, 0, 0},
            {0, 0, 0,  0, 4, 5,  7, 0, 0},
            {0, 0, 0,  1, 0, 0,  0, 3, 0},

            {0, 0, 1,  0, 0, 0,  0, 6, 8},
            {0, 0, 8,  5, 0, 0,  0, 1, 0},
            {0, 9, 0,  0, 0, 0,  4, 0, 0}
    };

    public static final int[][] SAMPLE_16 = {
        {0, 13, 16, 14,    10, 4, 0, 0,   0, 0, 0, 0,     0, 15, 2, 7},
        {0,  0,  4, 11,     0, 0, 0, 0,   8, 0, 0, 2,     0,  0, 0, 3},
        {0,  0,  9,  0,    16, 0, 3, 0,   0, 0, 0, 0,    14,  0, 0, 6},
        {6 , 0,  0,  0,     0, 0, 0, 11,  0, 3,  13, 0,   12, 0, 0, 5},

        {0, 0, 0, 0,      0,  0,  0, 0,    0, 0, 9, 0,     8, 0, 0, 0},
        {0, 5, 0,  10,    0, 7, 8,  6,     0, 0, 0, 14,    0, 0, 16, 0},
        {0, 7, 0, 0,      0, 10, 16, 12,  13, 0, 2,  0,    1, 5, 0, 14},
        {0, 4, 0, 12,     5,  0, 11,  9,   0, 0, 0, 6,     10, 0, 3, 0},

        {0, 12, 0, 0,     0, 0, 0,  4,    0, 0, 0, 8,    5, 0, 15, 10},
        {0, 0, 11, 13,    7, 9, 5, 10,    0, 0, 0, 4,    16, 0, 0, 0 },
        {14, 10, 0, 0,    0, 0, 0, 0,     0, 0, 0, 3,    0, 0, 0, 12},
        {0, 0, 0, 16,     0, 0, 2, 0,     0, 0, 1, 0,    0, 6, 14, 0},

        {9, 0, 13, 0,     0, 0, 0, 0,     0, 0, 0, 0,     0, 8, 0, 0},
        {0, 0, 7, 4,      2, 13, 6, 16,   0, 0, 0, 0,     0, 0, 0, 11},
        {0, 3, 0, 0,      0, 14, 0, 0,    0, 5, 0, 0,     6, 0, 0, 4},
        {2, 0, 0, 6,      11, 0, 0, 0,    12, 0, 0, 0,    9, 0, 7, 16}
    };

    /** you should never instantiate this static class.  */
    private Data() {}
}
