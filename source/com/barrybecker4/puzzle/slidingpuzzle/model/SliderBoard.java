// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.model;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Immutable representation of a slider board.
 * @author Barry Becker
 */
public class SliderBoard {

    /** Size of the board edge. If size = 4, then there will be 16-1 = 15 tiles. */
    private byte size;

    /** Represents the four different directions that a tile can move */
    private static final List<Integer> INDICES = Arrays.asList(0, 1, 2, 3);

    private final byte[][] tiles;

    private byte hamming;
    private short manhattan;


    /**
     * Constructor to create a slider configuration in the goal configuration.
     */
    public SliderBoard(int size) {
        this(size, false);
    }

    /**
     * Constructor.
     * @param shuffle if true then the created slider will have the tiles shuffled,
     *                else they will be in the goal state.
     */
    public SliderBoard(int size, boolean shuffle) {
        this.size = (byte)size;
        tiles = new byte[size][size];
        initializeTiles();
        if (shuffle) shuffleTiles();
        this.hamming = -1;
        this.manhattan = calculateManhattan();
    }

    public SliderBoard(int[][] tiles) {
        this.size = (byte)tiles.length;
        this.tiles = new byte[size][size];
        for (byte row=0; row < size; row++) {
            for (byte col=0; col < size; col++) {
                this.tiles[row][col] = (byte) tiles[row][col];
            }
        }
        this.hamming = -1;
        this.manhattan = calculateManhattan();
    }

    /**
     * Copy constructor.
     */
    public SliderBoard(SliderBoard board) {
        this.size = board.size;
        tiles = new byte[size][size];
        initializeTiles();
        for (byte i=0; i<size; i++) {
            System.arraycopy(board.tiles[i], 0, tiles[i], 0, size);
        }
        this.hamming = board.hamming;
        this.manhattan = board.manhattan;
    }

    private void initializeTiles() {
        byte ct = 1;
        for (byte row=0; row < size; row++) {
            for (byte col=0; col < size; col++) {
                tiles[row][col] = ct++;
            }
        }
        tiles[size-1][size-1] = 0;
    }

    /**
     * Constructor
     * create a new Slider by applying a move to another Slider.
     * Applying the same move a second time will undo it because it just swaps tiles.
     */
    public SliderBoard(SliderBoard pos, SlideMove move) {
        this(pos);
        applyMove(move);
        this.hamming = -1;
        this.manhattan = calculateManhattan();
    }

    public byte getHamming() {
        if (hamming == -1) {
            hamming = calculateHamming();
        }
        return hamming;
    }

    public short getManhattan() {
        return manhattan;
    }

    private byte calculateHamming() {
        byte expected = 0;
        byte hamCount = 0;
        for (byte i=0; i < size; i++) {
            for (byte j=0; j < size; j++) {
                byte value = tiles[i][j];
                expected++;
                if (value != 0 && value != expected) {
                    hamCount++;
                }
            }
        }
        return hamCount;
    }

    private short calculateManhattan() {
        short totalDistance = 0;
        for (byte i=0; i < size; i++) {
            for (byte j=0; j < size; j++) {
                int value = tiles[i][j];
                if (value != 0) {
                    int expCol = (value - 1) % size;
                    int expRow = (value - 1) / size;
                    int deltaRow = Math.abs(expRow - i);
                    int deltaCol = Math.abs(expCol - j);
                    totalDistance += deltaRow + deltaCol;
                }
            }
        }
        return totalDistance;
    }


    public byte getSize() {
        return size;
    }

    public byte getPosition(byte row, byte col) {
        return tiles[row][col];
    }

    /**
     * If the tiles are randomly placed, it is not guaranteed that there will be a solution.
     * See http://en.wikipedia.org/wiki/15_puzzle#CITEREFJohnsonStory1879
     * To shuffleUntilSorted, move tiles around until the blank position has been everywhere.
     */
    private synchronized void shuffleTiles() {

        Set<Location> visited = new HashSet<>();
        Location blankLocation = getEmptyLocation();
        visited.add(blankLocation);

        int numTiles = size * size;
        List<Integer> indices = new ArrayList<>(INDICES);

        while (visited.size() < numTiles) {
            Collections.shuffle(indices, MathUtil.RANDOM);
            Location loc;
            int ct = 0;
            do {
                loc = blankLocation.incrementOnCopy(MoveGenerator.OFFSETS[indices.get(ct++)]);
            } while (!isValidPosition(loc));

            SlideMove move = new SlideMove(blankLocation, loc);
            applyMove(move);
            blankLocation = loc;
            visited.add(blankLocation);
        }
    }

    private void applyMove(SlideMove move) {
        byte fromRow = move.getFromRow();
        byte fromCol = move.getFromCol();
        byte toRow = move.getToRow();
        byte toCol = move.getToCol();

        byte value = getPosition(fromRow, fromCol);
        setPosition(fromRow, fromCol, getPosition(toRow, toCol));
        setPosition(toRow, toCol, value);
    }

    /**
     * Private so others can not modify our immutable state after construction.
     */
    private void setPosition(byte row, byte col, byte val) {
        tiles[row][col] = val;
    }

    /**
     * @return true if the coordinates refer to one of the tiles.
     */
    public boolean isValidPosition(Location loc) {
        return loc.getRow() >= 0
                && loc.getRow() < size
                && loc.getCol() >= 0
                && loc.getCol() < size;
    }

    /**
     * @return true if all the tiles, when read across and down, are in increasing order.
     */
    public boolean isSolved() {
        return getHamming() == 0;
    }

    /**
     * Creates a new board with the move applied.
     * Does not violate immutability.
     */
    public SliderBoard doMove(SlideMove move) {
        return new SliderBoard(this, move);
    }

    /**
     * @return the position of the empty space (there is only one).
     */
    public Location getEmptyLocation() {
        for (byte i = 0; i < size; i++) {
            for (byte j = 0; j < size; j++) {
                if (getPosition(i, j) == 0) {
                    return new ByteLocation(i, j);
                }
            }
        }
        throw new IllegalStateException("There should have been a blank space in\n" + toString());
    }

    public int distanceToGoal() {
        return manhattan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SliderBoard)) return false;

        SliderBoard board = (SliderBoard) o;
        if (size != board.size) return false;

        if (this.getHamming() != board.getHamming()) {
            return false;
        }
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (tiles[i][j] != board.tiles[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(tiles);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Slider:\n");
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
               builder.append(tiles[i][j]).append(',');
            }
            builder.append('\n');
        }

        return builder.toString();
    }
}
