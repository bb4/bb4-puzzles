// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *  Stack of GenStates to try during the search.
 *
 *  @author Barry Becker
 */
public class StateStack extends LinkedList<GenState> {

    /**
     * From currentPosition, try moving in each direction in a random order.
     * Assigning different probabilities to the order in which we check these directions
     * can give interesting effects.
     */
    public void pushMoves(Location currentPosition, Location currentDir, int depth) {

        List<Direction> directions = getShuffledDirections();

        // check all the directions except the one we came from
        for ( int i = 0; i < 3; i++ ) {
            Direction direction = directions.get(i);
            Location movement = direction.apply(currentDir);
            this.add(0, new GenState(currentPosition, movement, depth));
        }
    }

    /**
     * return a shuffled list of directions
     * they are ordered given the potentially skewed probabilities at the top.
     */
    private List<Direction> getShuffledDirections() {
        double rnd = MathUtil.RANDOM.nextDouble();
        List<Direction> directions = new ArrayList<Direction>();
        List<Direction> originalDirections = new ArrayList<Direction>();
        originalDirections.addAll(Arrays.asList(Direction.values()));

        double fwdProb = Direction.FORWARD.getProbability();
        double leftProb = Direction.LEFT.getProbability();
        double rightProb = Direction.RIGHT.getProbability();
        double sum = fwdProb + leftProb + rightProb;
        fwdProb /= sum;
        leftProb /= sum;

        if (rnd < fwdProb) {
            directions.add( originalDirections.remove( 0 ) );
            directions.add( getSecondDir( originalDirections,  leftProb));
        }
        else if ( rnd >= fwdProb && rnd < ( fwdProb + leftProb) ) {
            directions.add( originalDirections.remove( 1 ) );
            directions.add( getSecondDir( originalDirections,  fwdProb));
        }
        else {
            directions.add( originalDirections.remove( 2 ) );
            directions.add( getSecondDir( originalDirections,  fwdProb));
        }
        // the third direction is whatever remains
        directions.add( originalDirections.remove( 0 ) );
        return directions;
    }

    /**
     * Determine the second direction in the list given a probability
     * @return  the second direction.
     */
    private Direction getSecondDir( List<Direction> twoDirections, double p1) {
        double rnd = MathUtil.RANDOM.nextDouble();
        if ( rnd < p1 )
            return twoDirections.remove( 0 );
        else
            return twoDirections.remove( 1 );
    }
}
