// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails;

import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.twopails.model.MoveGenerator;
import com.barrybecker4.puzzle.twopails.model.PailParams;
import com.barrybecker4.puzzle.twopails.model.Pails;
import com.barrybecker4.puzzle.twopails.model.PourOperation;
import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * Two pails puzzle Controller.
 * See http://www.cut-the-knot.org/ctk/CartWater.shtml#solve
 *     http://demonstrations.wolfram.com/WaterPouringProblem/
 * Inspired by Peter Norvig's "Design of Computer Programs" class on Udacity.
 *
 * @author Barry Becker
 */
public class TwoPailsPuzzleController extends AbstractPuzzleController<Pails, PourOperation> {

    private static final PailParams DEFAULT_PARAMS = new PailParams(9, 4, 6);

    private Pails initialPosition;

    /**
     * @param ui shows the current state on the screen.
     */
    public TwoPailsPuzzleController(Refreshable<Pails, PourOperation> ui) {
        super(ui);
        initialPosition = new Pails(DEFAULT_PARAMS);
        // set default
        algorithm_ = Algorithm.A_STAR_SEQUENTIAL;
    }

    /**
     * Puzzle parameters determine the size of the containers and target measure.
     * @param params new parameters to use when solving
     */
    public void setParams(PailParams params) {
        initialPosition = new Pails(params);
        if (ui_ != null) {
            ui_.refresh(initialPosition, 0);
        }
    }

    @Override
    public Pails initialState() {
        return initialPosition;
    }

    @Override
    public boolean isGoal(Pails position) {
        return position.isSolved();
    }

    @Override
    public Seq<PourOperation> legalTransitions(Pails position) {
        return JavaConversions.asScalaBuffer(new MoveGenerator(position).generateMoves()).toSeq();
    }

    @Override
    public Pails transition(Pails position, PourOperation move) {
        return position.doMove(move, false);
    }
}
