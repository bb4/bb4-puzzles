/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.maze.model;

import com.barrybecker4.common.geometry.IntLocation;
import com.barrybecker4.common.geometry.Location;


/**
 * Possible directions that we can go.
 * Vary the probability that each direction occurs for interesting effects.
 * the sum of these probabilities must sum to 1.
 *
 * @author Barry Becker
 */
public enum Direction {

    FORWARD(0.5) {
        @Override
        public Location apply(Location p) { return p; }
    },
    LEFT(0.28) {
        @Override
        public Location apply(Location p) { return leftOf(p); }
    },
    RIGHT(0.22) {
        @Override
        public Location apply(Location p) { return rightOf(p); }
    };

    private double probability_;


    Direction(double probability) {
        probability_ = probability;
    }

    public double getProbability() {
        return probability_;
    }

    public void setProbability(double probability) {
        probability_ = probability;
    }

    public abstract Location apply(Location dir);


    /**
     *  find the direction which is counterclockwise 90 (to the left) of the specified dir.
     */
    protected Location leftOf( Location dir ) {
        IntLocation newDir;
        if ( dir.getX() == 0 ) {
            newDir = new IntLocation(0, (dir.getY() > 0)? -1 : 1 );
        }
        else {  // assumed dir.y == 0
            newDir = new IntLocation(( dir.getX() > 0)? 1 : -1, 0);
        }
        return newDir;
    }

    /**
     * find the direction which is clockwise 90 (to the right) of the specified dir.
     */
    protected Location rightOf( Location dir ) {
        IntLocation newDir ;
        if ( dir.getX() == 0 ) {
            newDir = new IntLocation(0, (dir.getY() > 0)? 1 : -1);
        }
        else {  // assumed dir.y == 0
            newDir = new IntLocation((dir.getX() > 0)? -1 : 1, 0);
        }
        return newDir;
    }
}
