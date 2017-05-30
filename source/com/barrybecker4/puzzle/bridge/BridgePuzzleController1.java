// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge;

import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.bridge.model.*;
import com.barrybecker4.puzzle.common1.ui.AbstractPuzzleController;
import scala.collection.Seq;

/**
 * Bridge1 crossing Puzzle Controller.
 * See puzzle.common for puzzle framework classes.
 * See http://en.wikipedia.org/wiki/Bridge_and_torch_problem#cite_note-eatcs-4
 * See http://page.mi.fu-berlin.de/rote/Papers/pdf/Crossing+the+bridge+at+night.pdf
 * for analysis of general problem.
 * Inspired by Peter Norvig's "Design of Computer Programs" class on Udacity.
 *
 * @author Barry Becker
 */
public class BridgePuzzleController1 extends AbstractPuzzleController<Bridge1, BridgeMove1> {

    /** this is the standard bridge crossing problem with 4 people */
    private static final Integer[] DEFAULT_PEOPLE =
            InitialConfiguration1.STANDARD_PROBLEM.getPeopleSpeeds();

    private Bridge1 initialPosition;

    /**
     * @param ui shows the current state on the screen.
     */
    public BridgePuzzleController1(Refreshable<Bridge1, BridgeMove1> ui) {
        super(ui);
        initialPosition = new Bridge1(DEFAULT_PEOPLE);
        algorithm_ = Algorithm1.A_STAR_SEQUENTIAL;
        if (ui_ != null) ui_.refresh(initialPosition, 0);
    }

    public void setConfiguration(Integer[] config) {
        initialPosition = new Bridge1(config);
        if (ui_ != null) ui_.refresh(initialPosition, 0);
    }

    @Override
    public Bridge1 initialState() {
        return initialPosition;
    }

    @Override
    public boolean isGoal(Bridge1 position) {
        return position.isSolved();
    }

    @Override
    public Seq<BridgeMove1> legalTransitions(Bridge1 position) {
        return new MoveGenerator1(position).generateMoves();
    }

    @Override
    public Bridge1 transition(Bridge1 position, BridgeMove1 move) {
        return position.applyMove(move, false);
    }

    /**
     * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
     */
    public int distanceFromGoal(Bridge1 position) {
        return position.distanceFromGoal();
    }

    public int getCost(BridgeMove1 move) {
        return move.getCost();
    }
}
