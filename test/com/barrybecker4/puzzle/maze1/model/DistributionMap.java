package com.barrybecker4.puzzle.maze1.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Barry Becker
 */
public class DistributionMap extends HashMap<Direction, List<Integer>> {

    DistributionMap() {
        this.put(Direction.FORWARD, Arrays.asList(0, 0, 0));
        this.put(Direction.LEFT, Arrays.asList(0, 0, 0));
        this.put(Direction.RIGHT, Arrays.asList(0, 0, 0));
    }

    DistributionMap(List<Integer> forwardDist, List<Integer> leftDist, List<Integer> rightDist) {
        this.put(Direction.FORWARD, forwardDist);
        this.put(Direction.LEFT, leftDist);
        this.put(Direction.RIGHT, rightDist);
    }

    void increment(List<Direction> directions) {
        increment(directions.get(0), 0);
        increment(directions.get(1), 1);
        increment(directions.get(2), 2);
    }

    void increment(Direction dir, int position) {
        get(dir).set(position, get(dir).get(position) + 1);
    }
}
