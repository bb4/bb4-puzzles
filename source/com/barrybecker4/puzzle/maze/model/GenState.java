/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
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
    private Location direction_;
    private int depth_;

    public GenState(Location pos, Location dir, int d ) {
        position_ = pos;
        direction_ = dir;
        depth_ = d;
    }

    public Location getPosition() {
        return position_;
    }

    public Location getDirection() {
        return direction_;
    }

    public int getDepth() {
        return depth_;
    }

    public String toString() {
        return "[pos=" + position_ + " dir="+ direction_ + " depth="+ depth_ + "]";  // NON-NLS
    }
}


