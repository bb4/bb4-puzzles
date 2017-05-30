// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.puzzle.common1.AlgorithmEnum;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.solver.AStarConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.AStarPuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.ConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.PuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.SequentialPuzzleSolver;
import com.barrybecker4.puzzle.hiq.model.PegBoard1;
import com.barrybecker4.puzzle.hiq.model.PegMove1;

/**
 * Type of HiQ solver to use.
 *
 * @author Barry Becker
 */
public enum Algorithm1 implements AlgorithmEnum<PegBoard1, PegMove1> {

    SIMPLE_SEQUENTIAL,
    A_STAR_SEQUENTIAL,
    A_STAR_CONCURRENT,
    CONCURRENT_BREADTH,
    CONCURRENT_DEPTH,
    CONCURRENT_OPTIMUM;

    private String label;

    /**
     * Private constructor
     */
    Algorithm1() {
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
    public PuzzleSolver<PegMove1> createSolver(PuzzleController<PegBoard1, PegMove1> controller) {

        switch (this) {
            case SIMPLE_SEQUENTIAL :
                return new SequentialPuzzleSolver<>(controller);
            case A_STAR_SEQUENTIAL :
                return new AStarPuzzleSolver<>(controller);
            case A_STAR_CONCURRENT :
                return new AStarConcurrentPuzzleSolver<>(controller);
            case CONCURRENT_BREADTH :
                return new ConcurrentPuzzleSolver<>(controller, 0.4f);
            case CONCURRENT_DEPTH :
                return new ConcurrentPuzzleSolver<>(controller, 0.12f);
            case CONCURRENT_OPTIMUM :
                return new ConcurrentPuzzleSolver<>(controller, 0.2f);
        }
        return null;
    }

}
