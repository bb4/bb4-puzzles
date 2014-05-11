// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model;

import com.barrybecker4.common.geometry.Location;

import java.util.LinkedList;
import java.util.List;

/**
 *  Stack of GenStates to try during the search.
 *
 *  @author Barry Becker
 */
public class StateStack extends LinkedList<GenState> {

    Probabilities probabilities;

    /**
     * Constructor
     * @param probs the probabilities of moving each direction.
     */
    public StateStack(Probabilities probs) {
        probabilities = probs;
    }

    /**
     * Constructor where the probabilities for the three directions are assumed to be equal.
     */
    public StateStack() {
        probabilities = new Probabilities(1.0, 1.0, 1.0);
    }

    /**
     * From currentPosition, try moving in each direction in a random order.
     * Assigning different probabilities to the order in which we check these directions
     * can give interesting effects.
     */
    public void pushMoves(Location currentPosition, Location currentDir, int depth) {

        List<Direction> directions = probabilities.getShuffledDirections();

        // check all the directions except the one we came from
        for ( int i = 0; i < 3; i++ ) {
            Direction direction = directions.get(i);
            Location movement = direction.apply(currentDir);
            this.add(0, new GenState(currentPosition, movement, depth));
        }
    }
}
