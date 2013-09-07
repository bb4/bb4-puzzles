/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge;

import com.barrybecker4.puzzle.bridge.model.Bridge;
import com.barrybecker4.puzzle.bridge.model.BridgeMove;
import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.bridge.model.MoveGenerator;

import java.util.List;

/**
 * Bridge crossing Puzzle Controller.
 * See puzzle.common for puzzle framework classes.
 * See http://en.wikipedia.org/wiki/Bridge_and_torch_problem#cite_note-eatcs-4
 * See http://page.mi.fu-berlin.de/rote/Papers/pdf/Crossing+the+bridge+at+night.pdf
 * for analysis of general problem.
 *
 * @author Barry Becker
 */
public class BridgePuzzleController extends AbstractPuzzleController<Bridge, BridgeMove> {

    /* this is the standard bridge crossing problem with 4 people */
    private static final Integer[] DEFAULT_PEOPLE = new Integer[] {1, 2, 5, 8};

    private Bridge initialPosition;

    /**
     * @param ui shows the current state on the screen.
     */
    public BridgePuzzleController(Refreshable<Bridge, BridgeMove> ui) {
        super(ui);
        initialPosition = new Bridge(DEFAULT_PEOPLE);
        algorithm_ = Algorithm.A_STAR_SEQUENTIAL;
        ui_.refresh(initialPosition, 0);
    }

    public void setConfiguration(Integer[] config) {
        initialPosition = new Bridge(config);
        ui_.refresh(initialPosition, 0);
    }

    @Override
    public Bridge initialPosition() {
        return initialPosition;
    }

    @Override
    public boolean isGoal(Bridge position) {
        return position.isSolved();
    }

    @Override
    public List<BridgeMove> legalMoves(Bridge position) {
        return new MoveGenerator(position).generateMoves();
    }

    @Override
    public Bridge move(Bridge position, BridgeMove move) {
        return position.applyMove(move);
    }

    /**
     * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
     */
    public int distanceFromGoal(Bridge position) {
        return position.distanceFromGoal();
    }
}
