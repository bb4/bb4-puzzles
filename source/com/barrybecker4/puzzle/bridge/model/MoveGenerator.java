// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Sliding Puzzle move generator. Generates valid next moves.
 *
 * @author Barry Becker
 */
public class MoveGenerator  {

    Bridge board;

    /**
     * Constructor
     */
    public MoveGenerator(Bridge board) {
        this.board = board;
    }

    /**
     * Next moves are all the tiles that can slide into the current empty position.
     * @return List of all valid tile slides
     */
    public List<BridgeMove> generateMoves() {

        return board.isLightCrossed()?
                createMoves(board.getCrossedPeople(), false) :
                createMoves(board.getUncrossedPeople(), true);
    }

    private List<BridgeMove> createMoves(List<Integer> people, boolean crossing) {
        List<BridgeMove> moves = new LinkedList<>();

        int numPeople = people.size();
        moves.add(new BridgeMove(Arrays.asList(people.get(0)), crossing));

        if (numPeople > 1) {
            for (int i = 0; i < numPeople-1; i++) {
                for (int j = i + 1; j < numPeople; j++) {
                    moves.add(new BridgeMove(Arrays.asList(people.get(i), people.get(j)), crossing));
                }
                moves.add(new BridgeMove(Arrays.asList(people.get(i + 1)), crossing));
            }
        }

        return moves;
    }
}
