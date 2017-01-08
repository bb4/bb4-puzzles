// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model;


import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.puzzle.common.model.Move;

/**
 * Definition for a peg jumping another peg.
 * Immutable.
 *@author Barry Becker
 */
public final class PegMove1 implements Move {

    /* the position to move to */
    private Location toPosition;

    /** The position we moved from. */
    private Location fromPosition;


    /**
     * create a move object representing a transition on the board.
     * A naive implementation might use 4 four byte integers to store the from and to values.
     * This would use 16 bytes of memory per move.
     * If we do this, we will quickly run out of memory because fo the vast numbers of moves that must be stored.
     * I will use just 1 byte to store the move information.
     * All we need to know is the from position (which can be stored in 6 bits) and the to direction (which can be stored in 2 bits)
     * I know that a jump is always 2 spaces.
     */
    PegMove1(byte fromRow, byte fromCol,
             byte destinationRow, byte destinationCol) {
        fromPosition = new ByteLocation(fromRow, fromCol);
        toPosition = new ByteLocation(destinationRow, destinationCol);
    }

    PegMove1(Location fromPosition,
             Location destinationPosition) {
        this.fromPosition = fromPosition.copy();
        this.toPosition = destinationPosition.copy();
    }

    /**
     * @return a deep copy.
     */
    public PegMove1 copy() {
        return new PegMove1(fromPosition, toPosition);
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("from ").append(fromPosition).append(" to ");
        s.append(toPosition);
        return s.toString();
    }
}

