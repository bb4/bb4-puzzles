// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle1;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.puzzle.common1.AlgorithmEnum;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.solver.AStarConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.AStarPuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.ConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.PuzzleSolver;
import com.barrybecker4.puzzle.common1.solver.SequentialPuzzleSolver;
import com.barrybecker4.puzzle.slidingpuzzle1.model.SlideMove;
import com.barrybecker4.puzzle.slidingpuzzle1.model.SliderBoard;

/**
 * Type of solver to use.
 *
 * @author Barry Becker
 */
public enum Algorithm implements AlgorithmEnum<SliderBoard, SlideMove> {

    A_STAR_SEQUENTIAL,
    A_STAR_CONCURRENT,
    SIMPLE_SEQUENTIAL,
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
    public PuzzleSolver<SlideMove> createSolver(
            PuzzleController<SliderBoard, SlideMove> controller) {

        switch (this) {
            case A_STAR_SEQUENTIAL :
                return new AStarPuzzleSolver<>(controller);
            case A_STAR_CONCURRENT :
                return new AStarConcurrentPuzzleSolver<>(controller);
            case SIMPLE_SEQUENTIAL :
                // this will find a solution, but not necessary the shortest path
                return new SequentialPuzzleSolver<>(controller);
            case CONCURRENT_BREADTH :
                // this will find the shortest path to a solution if one exists, but takes longer
                return new ConcurrentPuzzleSolver<>(controller, 1.0f);
            case CONCURRENT_DEPTH :
                return new ConcurrentPuzzleSolver<>(controller, 0.12f);
            case CONCURRENT_OPTIMUM :
                return new ConcurrentPuzzleSolver<>(controller, 0.3f);
        }
        return null;
    }

}
