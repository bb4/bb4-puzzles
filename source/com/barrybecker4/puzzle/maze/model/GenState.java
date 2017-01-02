// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.model;

import com.barrybecker4.common.geometry.Location;

/**
 *  The state space position, depth, and direction while searching.
 *  Immutable.
 *
 *  @author Barry Becker
 */
public class GenState {

    private Location position_;
    private Location movement_;
    private int depth_;

    /**
     * Constructor
     * @param pos current position
     * @param movement movement to make relative to current position.
     * @param depth depth in the search
     */
    public GenState(Location pos, Location movement, int depth ) {
        position_ = pos;
        movement_ = movement;
        depth_ = depth;
    }

    public Location getPosition() {
        return position_;
    }

    /** The amount to move relative to the current position */
    public Location getRelativeMovement() {
        return movement_;
    }

    public int getDepth() {
        return depth_;
    }

    public String toString() {
        return "[pos=" + position_ + " move="+ movement_ + " depth="+ depth_ + "]";  // NON-NLS
    }
}


