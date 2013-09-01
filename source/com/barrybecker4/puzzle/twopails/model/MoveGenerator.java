// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.model;

import java.util.LinkedList;
import java.util.List;

import static com.barrybecker4.puzzle.twopails.model.PourOperation.Action;
import static com.barrybecker4.puzzle.twopails.model.PourOperation.Action.*;
import static com.barrybecker4.puzzle.twopails.model.PourOperation.Container.*;

/**
 * Two pails puzzle move generator. Generates valid next moves.
 * There are at most 6 moves which could be generated.
 *
 * @author Barry Becker
 */
public class MoveGenerator  {

    Pails pails;

    /**
     * Constructor
     */
    public MoveGenerator(Pails pails) {
        this.pails = pails;
    }

    /**
     * Next moves are all the pour operations that make sense given the current pail fill states.
     * For example, it does not make sense to empty and empty pail.
     * @return List of all valid tile slides
     */
    public List<PourOperation> generateMoves() {

        List<PourOperation> moves = new LinkedList<>();
        for (Action action : Action.values()) {

            switch(action) {
                case EMPTY :
                    if (pails.getFill1() > 0) moves.add(new PourOperation(EMPTY, FIRST));
                    if (pails.getFill2() > 0) moves.add(new PourOperation(EMPTY, SECOND));
                    break;
                case FILL :
                    if (pails.pail1HasRoom()) moves.add(new PourOperation(FILL, FIRST));
                    if (pails.pail2HasRoom()) moves.add(new PourOperation(FILL, SECOND));
                    break;
                case TRANSFER:
                    if (pails.getFill1() > 0 && pails.pail2HasRoom()) moves.add(new PourOperation(TRANSFER, FIRST));
                    if (pails.getFill2() > 0 && pails.pail1HasRoom()) moves.add(new PourOperation(TRANSFER, SECOND));
                    break;
            }
        }
        return moves;
    }
}
