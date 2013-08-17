/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.solver.ConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver;
import com.barrybecker4.puzzle.slidingpuzzle.model.Board;
import com.barrybecker4.puzzle.slidingpuzzle.model.Move;

/**
 * Type of solver to use.
 *
 * @author Barry Becker
 */
public enum Algorithm implements AlgorithmEnum<Board, Move> {

    SEQUENTIAL,
    CONCURRENT_BREADTH,
    CONCURRENT_DEPTH,
    CONCURRENT_OPTIMUM;

    private String label;

    /**
     * Private constructor
     */
    Algorithm() {
        this.label = AppContext.getLabel(this.name());
    }

    @Override
    public String getLabel() {
        return label;
    }


    /**
     * Create an instance of the algorithm given the controller and a refreshable.
     */
    @Override
    public PuzzleSolver<Board, Move> createSolver(PuzzleController<Board, Move> controller, Refreshable<Board, Move> ui) {

        switch (this) {
            case SEQUENTIAL :
                return new SequentialPuzzleSolver<Board, Move>(controller, ui);
            case CONCURRENT_BREADTH :
                return new ConcurrentPuzzleSolver<Board, Move>(controller, 0.4f, ui);
            case CONCURRENT_DEPTH :
                return new ConcurrentPuzzleSolver<Board, Move>(controller, 0.12f, ui);
            case CONCURRENT_OPTIMUM :
                return new ConcurrentPuzzleSolver<Board, Move>(controller, 0.2f, ui);
        }
        return null;
    }

}
