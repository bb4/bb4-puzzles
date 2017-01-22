// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle1.model;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.common.model.Move;

/**
 * Definition for tile that slides around on the Slider board.
 * Immutable.
 * @author Barry Becker
 */
public final class SlideMove implements Move {

    /* the position to move to */
    private Location toPosition;

    /** The position we moved from. */
    private Location fromPosition;


    /**
     * create a move object representing a transition on the board.
     */
    public SlideMove(Location fromPosition,
              Location destinationPosition) {
        this.fromPosition = fromPosition.copy();
        this.toPosition = destinationPosition.copy();
    }

    /**
     * @return a deep copy.
     */
    public SlideMove copy() {
        return new SlideMove(fromPosition, toPosition);
    }

    byte getFromRow() {
        return (byte) fromPosition.getRow();
    }
    byte getFromCol() {
        return (byte) fromPosition.getCol();
    }

    byte getToRow() {
        return (byte) toPosition.getRow();
    }
    byte getToCol() {
        return (byte) toPosition.getCol();
    }

    /** @return the from and to positions */
    @Override
    public String toString() {
        return "from " + fromPosition + " to " + toPosition;
    }
}

