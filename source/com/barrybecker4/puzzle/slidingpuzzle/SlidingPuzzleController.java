/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle;

import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle.model.Board;
import com.barrybecker4.puzzle.slidingpuzzle.model.Move;
import com.barrybecker4.puzzle.slidingpuzzle.model.MoveGenerator;

import java.util.List;

/**
 * Sliding Puzzle Controller.
 * See puzzle.common for puzzle framework classes.
 *
 * @author Barry Becker
 */
public class SlidingPuzzleController extends AbstractPuzzleController<Board, Move> {

    private static final int DEFAULT_SIZE = 3;

    private Board initialPosition;

    /**
     * @param ui shows the current state on the screen.
     */
    public SlidingPuzzleController(Refreshable<Board, Move> ui) {
        super(ui);
        initialPosition = new Board(DEFAULT_SIZE);
        // set default
        algorithm_ = Algorithm.CONCURRENT_OPTIMUM;
    }

    @Override
    public Board initialPosition() {
        return initialPosition;
    }

    @Override
    public boolean isGoal(Board position) {
        return position.isSolved();
    }

    @Override
    public List<Move> legalMoves(Board position) {
        return new MoveGenerator(position).generateMoves();
    }

    @Override
    public Board move(Board position, Move move) {
        return position.doMove(move);
    }
}
