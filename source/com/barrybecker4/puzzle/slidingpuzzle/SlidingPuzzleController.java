/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle;

import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle.model.Slider;
import com.barrybecker4.puzzle.slidingpuzzle.model.Move;
import com.barrybecker4.puzzle.slidingpuzzle.model.MoveGenerator;

import java.util.List;

/**
 * Sliding Puzzle Controller.
 * See puzzle.common for puzzle framework classes.
 *
 * @author Barry Becker
 */
public class SlidingPuzzleController extends AbstractPuzzleController<Slider, Move> {

    private static final int DEFAULT_SIZE = 3;

    private Slider initialPosition;

    /**
     * @param ui shows the current state on the screen.
     */
    public SlidingPuzzleController(Refreshable<Slider, Move> ui) {
        super(ui);
        initialPosition = new Slider(DEFAULT_SIZE);
        // set default
        algorithm_ = Algorithm.CONCURRENT_OPTIMUM;
    }

    @Override
    public Slider initialPosition() {
        return initialPosition;
    }

    @Override
    public boolean isGoal(Slider position) {
        return position.isSolved();
    }

    @Override
    public List<Move> legalMoves(Slider position) {
        return new MoveGenerator(position).generateMoves();
    }

    @Override
    public Slider move(Slider position, Move move) {
        return position.doMove(move);
    }
}
