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
    SlideMove(Location fromPosition,
              Location destinationPosition) {
        this.fromPosition = fromPosition.copy();
        this.toPosition = destinationPosition.copy();
    }

    /**
     * @return a deep copy.
     */
    public com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove copy() {
        return new com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove(fromPosition, toPosition);
    }

    public byte getFromRow() {
        return (byte) fromPosition.getRow();
    }
    public byte getFromCol() {
        return (byte) fromPosition.getCol();
    }

    public byte getToRow() {
        return (byte) toPosition.getRow();
    }
    public byte getToCol() {
        return (byte) toPosition.getCol();
    }

    /** @return the from and to positions */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("from ").append(fromPosition).append(" to ");
        s.append(toPosition);
        return s.toString();
    }
}

