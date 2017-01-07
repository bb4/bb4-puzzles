// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.model;

import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Bridge1 Puzzle move generator. Generates valid next moves.
 *
 * @author Barry Becker
 */
public class MoveGenerator1 {

    private Bridge1 board;

    /**
     * Constructor
     */
    public MoveGenerator1(Bridge1 board) {
        this.board = board;
    }

    /**
     * Next moves are all the tiles that can slide into the current empty position.
     * @return List of all valid tile slides
     */
    public Seq<BridgeMove1> generateMoves() {

        return board.isLightCrossed()?
                createMoves(board.getCrossedPeople(), false) :
                createMoves(board.getUncrossedPeople(), true);
    }

    private Seq<BridgeMove1> createMoves(List<Integer> people, boolean crossing) {
        List<BridgeMove1> moves = new LinkedList<>();

        int numPeople = people.size();
        moves.add(new BridgeMove1(Arrays.asList(people.get(0)), crossing));

        if (numPeople > 1) {
            for (int i = 0; i < numPeople-1; i++) {
                for (int j = i + 1; j < numPeople; j++) {
                    moves.add(new BridgeMove1(Arrays.asList(people.get(i), people.get(j)), crossing));
                }
                moves.add(new BridgeMove1(Arrays.asList(people.get(i + 1)), crossing));
            }
        }

        // Put them in order of fastest to cross first. This speeds A*, but slows sequential a lot.
        //Collections.sort(moves);
        return JavaConversions.asScalaBuffer(moves).toSeq();
    }
}
