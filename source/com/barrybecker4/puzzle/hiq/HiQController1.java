// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq;

import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common1.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.hiq.model.MoveGenerator1;
import com.barrybecker4.puzzle.hiq.model.PegBoard1;
import com.barrybecker4.puzzle.hiq.model.PegMove1;
import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * HiQ Puzzle Controller.
 * See puzzle.common for puzzle framework classes.
 *
 * @author Barry Becker
 */
public class HiQController1 extends AbstractPuzzleController<PegBoard1, PegMove1> {

    /**
     * @param ui shows the current state on the screen.
     */
    public HiQController1(Refreshable<PegBoard1, PegMove1> ui) {
        super(ui);
        algorithm_ = Algorithm1.CONCURRENT_OPTIMUM;
    }

    @Override
    public PegBoard1 initialState() {
        return PegBoard1.INITIAL_BOARD_POSITION;
    }

    @Override
    public boolean isGoal(PegBoard1 position) {
        return position.isSolved();
    }

    @Override
    public Seq<PegMove1> legalTransitions(PegBoard1 position) {
        return JavaConversions.asScalaBuffer(new MoveGenerator1(position).generateMoves()).toSeq();
    }

    @Override
    public PegBoard1 transition(PegBoard1 position, PegMove1 move) {
        return position.doMove(move, false);
    }

    /**
     * A simple estimate of the future cost to the goal is the number of
     * pegs remaining. Other secondary factors like how spread out the remaining
     * pegs are may be used to improve this estimate.
     * @return estimate of the cost to reach the a single bag remaining
     */
    public int distanceFromGoal(PegBoard1 position) {
        return position.getNumPegsLeft();
    }

    /**
     * Check all board symmetries to be sure it has or has not been seen.
     * If it was never seen before add it.
     * Must be synchronized because some solvers use concurrency.
     */
    @Override
    public synchronized boolean alreadySeen(PegBoard1 position, scala.collection.mutable.Set<PegBoard1> seen) {

        boolean wasSeen = position.containedIn(seen);
        if (!wasSeen) {
            seen.add(position);
        }
        return wasSeen;
    }
}
