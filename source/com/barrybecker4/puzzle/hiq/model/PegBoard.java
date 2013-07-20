/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.hiq.model;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * Immutable representation of a PegBoard.
 * @author Barry Becker
 */
public class PegBoard {

    /** this must be odd */
    public static final byte SIZE = 7;

    /**
     *  The number of symmetries the board has.
     *  Each odd and even pair are mirror images of a 90 degree rotation.
     */
    public static final int SYMMETRIES = 8;

    /** maintains the compressed peg position information for the board. */
    private int bits_;                // the first 32 positions
    private boolean finalBit_;        // the final, 33rd position
    private boolean nextToFinalBit_;  // the final, 32rd position

    /**
     * The 8 fold symmetry of the board.
     */
    private static final byte[][] BOARD_SYMMETRY = {
        { /* placeholder for 0 index. i.e. 0-31 */},
        {2, 1, 0, 5, 4, 3, 12, 11, 10, 9, 8, 7, 6, 19, 18, 17, 16, 15, 14, 13, 26, 25, 24, 23, 22, 21, 20, 29, 28, 27, 32, 31, 30},
        {12, 19, 26, 11, 18, 25, 2, 5, 10, 17, 24, 29, 32, 1, 4, 9, 16, 23, 28, 31, 0, 3, 8, 15, 22, 27, 30, 7, 14, 21, 6, 13, 20},
        {26, 19, 12, 25, 18, 11, 32, 29, 24, 17, 10, 5, 2, 31, 28, 23, 16, 9, 4, 1, 30, 27, 22, 15, 8, 3, 0, 21, 14, 7, 20, 13, 6},
        {32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
        {30, 31, 32, 27, 28, 29, 20, 21, 22, 23, 24, 25, 26, 13, 14, 15, 16, 17, 18, 19, 6, 7, 8, 9, 10, 11, 12, 3, 4, 5, 0, 1, 2},
        {20, 13, 6, 21, 14, 7, 30, 27, 22, 15, 8, 3, 0, 31, 28, 23, 16, 9, 4, 1, 32, 29, 24, 17, 10, 5, 2, 25, 18, 11, 26, 19, 12},
        {6, 13, 20, 7, 14, 21, 0, 3, 8, 15, 22, 27, 30, 1, 4, 9, 16, 23, 28, 31, 2, 5, 10, 17, 24, 29, 32, 11, 18, 25, 12, 19, 26}
    };

    private static final byte NUM_PEG_HOLES = 33;
    private static final byte CENTER = 3;
    private static final byte CORNER_SIZE = 2;


    /** The initial board position constant */
    public static final PegBoard INITIAL_BOARD_POSITION = new PegBoard();
    static {
       for (byte i = 0; i<SIZE; i++) {
           for (byte j = 0; j<SIZE; j++) {
               if (PegBoard.isValidPosition(i, j)) {
                    INITIAL_BOARD_POSITION.setPosition(i, j, true);
               }
           }
       }
       INITIAL_BOARD_POSITION.setPosition(PegBoard.CENTER, PegBoard.CENTER, false);
    }

    /**
     * Do not use this constructor since outsiders cannot create mutable boards.
     */
    private PegBoard() {}

    /**
     * Copy constructor.
     */
    public PegBoard(PegBoard board) {
        bits_ = board.bits_;
        finalBit_ = board.finalBit_;
        nextToFinalBit_ = board.nextToFinalBit_;
    }

    /**
     * Constructor
     * create a new BoardPosition by applying a move to another BoardPosition.
     */
    public PegBoard(PegBoard pos, PegMove move, boolean undo) {
        this(pos);

        byte fromRow = move.getFromRow();
        byte fromCol = move.getFromCol();
        byte toRow = move.getToRow();
        byte toCol = move.getToCol();

        setPosition(fromRow, fromCol, undo);
        // Remove or replace the piece that was jumped as appropriate
        setPosition((byte)((fromRow + toRow) >> 1), (byte)((fromCol + toCol) >> 1), undo);
        setPosition(toRow, toCol, !undo);
    }

    public int getSize() {
        return SIZE;
    }

    public boolean getPosition(byte row, byte col) {
        return get(getIndexForPosition(row, col));
    }

    /**
     * Private so others can not modify our immutable state after construction.
     */
    private void setPosition(byte row, byte col, boolean val) {
        set(getIndexForPosition(row, col), val);
    }

    /**
     *@return true if the coordinates refer to one of the 33 board positions that can hold a peg.
     */
    public static boolean isValidPosition(int row , int col) {
        if (row < 0 || row  >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        return row >= CORNER_SIZE && row < SIZE - CORNER_SIZE
            || col >= CORNER_SIZE && col < SIZE - CORNER_SIZE;
    }

    public boolean isEmpty(byte row, byte col) {
        return !getPosition(row, col);
    }

    /**
     * Because of symmetry, there is really only one first move not 4.
     * @return PegMove the first move.
     */
    public PegMove getFirstMove() {
       return new PegMove(CENTER, (byte)(CENTER-2), CENTER, CENTER);
    }

    public boolean isSolved() {
        return (getNumPegsLeft() == 1 && getPosition(CENTER, CENTER));
    }

    /**
     * Creates a new board with the move applied.
     * Does not violate immutability.
     */
    public PegBoard doMove(PegMove move, boolean undo) {
        return new PegBoard(this, move, undo);
    }

    /**
     * @param pegged boolean if true get pegged locations, else empty locations
     * @return List of pegged or empty locations
     */
    public List<Location> getLocations(boolean pegged) {

        List<Location> list = new LinkedList<Location>();
        for (byte i = 0; i<SIZE; i++) {
            for (byte j = 0; j<SIZE; j++) {
                if (isValidPosition(i, j) && getPosition(i, j) == pegged) {
                    list.add(new ByteLocation(i, j));
                }
            }
        }
        return list;
    }

    /**
     * @return Map the coordinate location into our memory conserving hash.
     */
    private int getIndexForPosition(int row, int col) {
        int p = row * 10 + col;
        int index = -1;
        if (p>19 && p<47) {
            // this crazy formula gives the index for the middle 3 rows in the board.
            return p % 10 + (p / 10 - 1) * 7 - 1;
        }
        switch (p) {
            case  2: index = 0; break;
            case  3: index = 1; break;
            case  4: index = 2; break;
            case 12: index = 3; break;
            case 13: index = 4; break;
            case 14: index = 5; break;
            case 52: index = 27; break;
            case 53: index = 28; break;
            case 54: index = 29; break;
            case 62: index = 30; break;
            case 63: index = 31; break;
            case 64: index = 32; break;
            default: assert false: "invalid position row="+row +" col="+col;
        }
        return index;
    }

    /**
     * Set value of position in internal compress data structure.
     */
    private void set(int i, boolean val) {
        if (i == NUM_PEG_HOLES - 1) {
            finalBit_ = val;
        } else if (i == NUM_PEG_HOLES - 2)  {
            nextToFinalBit_ = val;
        }
        else {
            long place = 1 << i;
            bits_ -= get(i) ? place : 0;
            bits_ += val ? place : 0;
        }
    }

    /**
     * @return extract the value of the ith bit.
     */
    private boolean get(int i)  {
        if (i == NUM_PEG_HOLES - 1) {
            return finalBit_;
        }
        if (i == NUM_PEG_HOLES - 2) {
            return nextToFinalBit_;
        }
        long place = 1 << i;
        return ((bits_ & place) != 0);
    }

    /**
     * @return number of pegs left on the board.
     */
    public int getNumPegsLeft() {
        int nPegsLeft = 0;
        for (int i=0; i<NUM_PEG_HOLES; i++) {
            if (get(i)) {
                nPegsLeft++;
            }
        }
        return nPegsLeft;
    }

    /**
     * Check all 8 symmetries
     * if rotateIndex = 0 then no rotation
     * if rotateIndex = 1 mirror image of this,
     * if rotateIndex = 2 then 90 degree rotation of this,
     * if rotateIndex = 3 then mirror image of 2, etc
     * @return specified rotation of the board.
     */
    public PegBoard symmetry(int symmIndex) {
        return (symmIndex==0) ? this : rotate(BOARD_SYMMETRY[symmIndex]);
    }

    @Override
    public boolean equals(Object b) {
        PegBoard board = (PegBoard) b;
        return (bits_ == board.bits_ && finalBit_ == board.finalBit_ && nextToFinalBit_ == board.nextToFinalBit_);
    }

    /**
     * All but one bit accounted for in the hash.
     */
    @Override
    public int hashCode() {
        return nextToFinalBit_ ? -bits_ : bits_;
    }

    /**
     * Rotate the board according to symmetry.
     * Not all are rotational symmetries, but you get the idea....
     * @return specified rotation of the board.
     */
    private PegBoard rotate(byte[] rotateIndices) {
        PegBoard rotatedBoard = new PegBoard();
        for (int i = 0; i < NUM_PEG_HOLES; i++)  {
            rotatedBoard.set(i, get(rotateIndices[i]));
        }
        return rotatedBoard;
    }

    @Override
    public String toString() {
        StringBuilder buf= new StringBuilder(finalBit_?"1":"0");
        buf.append(nextToFinalBit_?"1":"0");
        buf.append(Integer.toBinaryString(bits_));
        return buf.toString();
    }
}
