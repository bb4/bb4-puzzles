/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.maze.model;

import com.barrybecker4.common.math.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The three normalized directional probabilities.
 * @author Barry Becker
 */
public class Probabilities {

    private double forwardProb;
    private double leftProb;
    private double rightProb;

    /**
     * Constructor that takes the 3 probabilities.
     */
    public Probabilities(double fwdProb, double leftProb, double rightProb) {

        double total = fwdProb + leftProb + rightProb;
        this.forwardProb = fwdProb / total;
        this.leftProb = leftProb / total;
        this.rightProb = rightProb / total;
    }

    /**
     * return a shuffled list of directions
     * they are ordered given the potentially skewed probabilities at the top.
     */
    public List<Direction> getShuffledDirections() {
        double rnd = MathUtil.RANDOM.nextDouble();
        List<Direction> directions = new ArrayList<>();
        List<Direction> originalDirections = new ArrayList<>();

        // the forward, left, and right directions
        originalDirections.addAll(Arrays.asList(Direction.values()));

        double sum = forwardProb + leftProb + rightProb;
        forwardProb /= sum;
        leftProb /= sum;

        if (rnd < forwardProb) {
            directions.add( originalDirections.remove( 0 ) );
            directions.add( getSecondDir( originalDirections,  leftProb/(leftProb + rightProb)));
        }
        else if ( rnd >= forwardProb && rnd < ( forwardProb + leftProb) ) {
            directions.add( originalDirections.remove( 1 ) );
            directions.add( getSecondDir( originalDirections, forwardProb/(forwardProb + rightProb)));
        }
        else {
            directions.add( originalDirections.remove( 2 ) );
            directions.add( getSecondDir( originalDirections, forwardProb/(forwardProb + leftProb)));
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
