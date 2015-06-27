/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.hiq;

import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.hiq.model.MoveGenerator;
import com.barrybecker4.puzzle.hiq.model.PegBoard;
import com.barrybecker4.puzzle.hiq.model.PegMove;

import java.util.List;
import java.util.Set;

/**
 * HiQ Puzzle Controller.
 * See puzzle.common for puzzle framework classes.
 *
 * @author Barry Becker
 */
public class HiQController extends AbstractPuzzleController<PegBoard, PegMove> {

    /**
     * @param ui shows the current state on the screen.
     */
    public HiQController(Refreshable<PegBoard, PegMove> ui) {
        super(ui);
        algorithm_ = Algorithm.CONCURRENT_OPTIMUM;
    }

    @Override
    public PegBoard initialPosition() {
        return PegBoard.INITIAL_BOARD_POSITION;
    }

    @Override
    public boolean isGoal(PegBoard position) {
        return position.isSolved();
    }

    @Override
    public List<PegMove> legalMoves(PegBoard position) {
        return new MoveGenerator(position).generateMoves();
    }

    @Override
    public PegBoard move(PegBoard position, PegMove move) {
        return position.doMove(move, false);
    }

    /**
     * A simple estimate of the future cost to the goal is the number of
     * pegs remaining. Other secondary factors like how spread out the remaining
     * pegs are may be used to improve this estimate.
     * @return estimate of the cost to reach the a single bag remaining
     */
    public int distanceFromGoal(PegBoard position) {
        return position.getNumPegsLeft();
    }

    /**
     * Check all board symmetries to be sure it has or has not been seen.
     * If it was never seen before add it.
     * Must be synchronized because some solvers use concurrency.
     */
    @Override
    public synchronized boolean alreadySeen(PegBoard position, Set<PegBoard> seen) {

        boolean wasSeen = position.containedIn(seen);
        if (!wasSeen) {
            seen.add(position);
        }
        return wasSeen;
    }
}
