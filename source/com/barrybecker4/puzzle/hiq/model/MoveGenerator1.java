// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.model;

import com.barrybecker4.common.geometry.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * HiQ Puzzle move generator. Generates valid next moves.
 *
 * @author Barry Becker
 */
public class MoveGenerator1 {

    PegBoard1 board;

    /**
     * Constructor
     */
    public MoveGenerator1(PegBoard1 board) {
        this.board = board;
    }

    /**
     * @return List of all valid jumps for the current board state
     */
    public List<PegMove1> generateMoves() {
        List<PegMove1> moves = new LinkedList<PegMove1>();
        List<Location> emptyLocations = board.getLocations(false);
        if (emptyLocations.isEmpty()) {
            moves.add(board.getFirstMove());
        } else {
            for (Location pos : emptyLocations) {
                moves.addAll(findMovesForLocation(pos, false));
            }
        }
        return moves;
    }

    /**
     *
     * @param location Location empty or peg location based on undo
     * @param undo boolean find undo (peg) or redo (empty location) moves.
     * @return List
     */
    private List<PegMove1> findMovesForLocation(Location location, boolean undo) {
        List<PegMove1> moves = new LinkedList<PegMove1>();
        byte r = (byte) location.getRow();
        byte c = (byte) location.getCol();

        // 4 cases to consider: NEWS
        checkMoveForDirection(r, c, 0, -2, undo, moves);
        checkMoveForDirection(r, c, 0, 2, undo, moves);
        checkMoveForDirection(r, c, -2, 0, undo, moves);
        checkMoveForDirection(r, c, 2, 0, undo, moves);
        return moves;
    }

    private void checkMoveForDirection(byte r, byte c, int rowOffset, int colOffset, boolean undo, List<PegMove1> moves) {
        byte fromRow = (byte)(r + rowOffset);
        byte fromCol = (byte)(c + colOffset);
        if (PegBoard1.isValidPosition(fromRow, fromCol)
             && board.getPosition(fromRow, fromCol)!=undo
             && board.getPosition((byte)(r + rowOffset/2), (byte)(c + colOffset/2))!=undo) {
            moves.add(new PegMove1(fromRow, fromCol, r, c));
        }
    }

}
