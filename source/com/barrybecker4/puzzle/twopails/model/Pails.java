/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.twopails.model;

import static com.barrybecker4.puzzle.twopails.model.PourOperation.Container.*;

/**
 * Immutable representation of the two Pails and the amount of liquid they each contain at the moment.
 * @author Barry Becker
 */
public class Pails {

    private PailParams params;
    private byte fill1;
    private byte fill2;

    /**
     * Constructor to create two empty pails.
     */
    public Pails(PailParams params) {
        this.params = params;
        fill1 = 0;
        fill2 = 0;
    }

    /**
     * Copy constructor.
     */
    public Pails(Pails pails) {
        params = pails.params;
        fill1 = pails.fill1;
        fill2 = pails.fill2;
    }

    public byte getFill1() {
        return fill1;
    }
    public byte getFill2() {
        return fill2;
    }

    boolean pail1HasRoom() {
        return fill1 < params.getPail1Size();
    }
    boolean pail2HasRoom() {
        return fill2 < params.getPail2Size();
    }

    public PailParams getParams() {
        return params;
    }

    /**
     * creates a new Pails by applying a move to another Pails.
     * Does not violate immutability.
     */
    public Pails doMove(PourOperation move, boolean undo) {
        return new Pails(this, move, undo);
    }

    /**
     * @param pos current state
     * @param move transition to apply to it
     * @param undo if true, then undoes the transition rather than applying it.
     */
    public Pails(Pails pos, PourOperation move, boolean undo) {
        this(pos);
        applyMove(move, undo);
    }

    private void applyMove(PourOperation move, boolean undo) {
        PourOperation op = undo? move.reverse() : move;

        switch (op.getAction()) {
            case FILL :
                if (op.getContainer() == FIRST)
                    fill1 = params.getPail1Size();
                else fill2 = params.getPail2Size();
                break;
            case EMPTY :
                if (op.getContainer() == FIRST)
                    fill1 = 0;
                else fill2 = 0;
                break;
            case TRANSFER:
                if (op.getContainer() == FIRST)  {
                    // transfer from first container to second
                    int space = params.getPail2Size() - fill2;
                    fill2 = (byte) Math.min(fill1 + fill2, params.getPail2Size());
                    fill1 = (byte) Math.max(0, fill1 - space);
                } else {
                    // transfer from second container to first
                    int space = params.getPail1Size() - fill1;
                    fill1 = (byte) Math.min(fill1 + fill2, params.getPail1Size());
                    fill2 = (byte) Math.max(0, fill2 - space);
                }
                break;
        }
    }

    /**
     * @return true if either container has exactly the target measure.
     */
    public boolean isSolved() {

        byte target = params.getTargetMeasureSize();
        return fill1 == target || fill2 == target;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Pails:");
        builder.append('[').append(fill1).append(" ").append(fill2).append(']');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pails pails = (Pails) o;
        return fill1 == pails.fill1 && fill2 == pails.fill2;

    }

    @Override
    public int hashCode() {
        int result = (int) fill1;
        result = 31 * result + (int) fill2;
        return result;
    }

}
