/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.model;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Immutable representation of a Slider.
 * @author Barry Becker
 */
public class Slider {

    /** Size of the board edge. If size = 4, then there will be 16-1 = 15 tiles. */
    private int size = 3;

    private int[][] tiles;

    /**
     * Do not use this constructor since outsiders cannot create mutable boards.
     */
    public Slider(int size) {
        this.size = size;
        tiles = new int[size][size];

        int numTiles = size * size;
        List<Integer> ints = new ArrayList<Integer>(numTiles);
        for (int i=0; i<numTiles; i++) {
            ints.add(i);
        }
        Collections.shuffle(ints, MathUtil.RANDOM);

        int ct = 0;
        for (byte i=0; i<size; i++) {
            for (byte j=0; j<size; j++) {
                tiles[i][j] = ints.get(ct++);
            }
        }
    }

    /**
     * Copy constructor.
     */
    public Slider(Slider board) {
        this(board.size);
        for (byte i=0; i<size; i++) {
            for (byte j=0; j<size; j++) {
                tiles[i][j] = board.tiles[i][j];
            }
        }
    }

    /**
     * Constructor
     * create a new Slider by applying a move to another Slider.
     * Applying the same move a second time will undo it because it just swaps tiles.
     */
    public Slider(Slider pos, Move move) {
        this(pos);

        byte fromRow = move.getFromRow();
        byte fromCol = move.getFromCol();
        byte toRow = move.getToRow();
        byte toCol = move.getToCol();

        int value = getPosition(fromRow, fromCol);
        setPosition(fromRow, fromCol, getPosition(toRow, toCol));
        setPosition(toRow, toCol, value);
    }

    public int getSize() {
        return size;
    }

    public int getPosition(byte row, byte col) {
        return tiles[row][col];
    }

    /**
     * Private so others can not modify our immutable state after construction.
     */
    private void setPosition(byte row, byte col, int val) {
        tiles[row][col] = val;
    }

    /**
     *@return true if the coordinates refer to one of the tiles.
     */
    public boolean isValidPosition(int row , int col) {
        return (row >= 0 && row  < size && col >= 0 && col < size);
    }

    /**
     * @return true if all the tiles, when read across and down, are in increasing order.
     */
    public boolean isSolved() {
        int last = -1;
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (tiles[i][j] < last)
                    return false;
                last = tiles[i][j];
            }
        }
        return true;
    }

    /**
     * Creates a new board with the move applied.
     * Does not violate immutability.
     */
    public Slider doMove(Move move) {
        return new Slider(this, move);
    }

    /**
     * @return the position of the empty space (there is only one).
     */
    public Location getEmptyLocation() {
        for (byte i = 0; i<size; i++) {
            for (byte j = 0; j<size; j++) {
                if (getPosition(i, j) == 0) {
                    return new ByteLocation(i, j);
                }
            }
        }
        throw new IllegalStateException("There should have been a blank space");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Slider)) return false;

        Slider board = (Slider) o;
        if (size != board.size) return false;

        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (tiles[i][j] != board.tiles[i][j]) return false;
            }
        }
        return true;
        //return size == board.size && Arrays.deepEquals(tiles, board.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder("Slider:");
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
               bldr.append(tiles[i][j]).append(',');
            }
        }

        return bldr.toString();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
