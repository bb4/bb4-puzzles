// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model;

/**
 * Find the distance to goal using manhattan distance.
 * This is one heuristic for estimating the distance to the goal. Others could be added using the strategy pattern.
 * @author Barry Becker
 */
public class ManhattanDistanceFinder {

    /**
     * @param board to evaluate
     * @return estimate of distance to solution.
     */
    public int findDistance(SliderBoard board) {
        byte size = board.getSize();


        int sum = 0;
        for (byte row = 0; row < size; row++) {
            for (byte col = 0; col < size; col++) {
                sum += distance(row, col, board.getPosition(row, col), size);
            }
        }
        return sum;
    }

    private int distance(int goalRow, int goalCol, byte value, byte size) {
        return Math.abs((value / size) - goalRow) + Math.abs((value % size) - goalCol);
    }
}
