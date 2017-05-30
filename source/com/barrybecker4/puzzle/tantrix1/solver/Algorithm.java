// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.puzzle.common1.AlgorithmEnum;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.solver.*;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;

/**
 * Enum for type of solver to employ when solving the puzzle.
 *
 * @author Barry Becker
 */
public enum Algorithm implements AlgorithmEnum<TantrixBoard, TilePlacement> {

    SIMPLE_SEQUENTIAL,
    A_STAR_SEQUENTIAL,
    A_STAR_CONCURRENT,
    CONCURRENT_BREADTH,
    CONCURRENT_DEPTH,
    CONCURRENT_OPTIMUM,
    GENETIC_SEARCH,
    CONCURRENT_GENETIC_SEARCH;

    private String label;

    /**
     * Private constructor
     * Creates a new instance of Algorithm1
     */
    private Algorithm() {
        this.label = AppContext.getLabel(this.name());
    }

    public String getLabel() {
        return label;
    }

    /**
     * Create an instance of the algorithm given the controller and a refreshable.
     */
    public PuzzleSolver<TilePlacement> createSolver(
            PuzzleController<TantrixBoard, TilePlacement> controller) {

        switch (this) {
            case SIMPLE_SEQUENTIAL :
                return new SequentialPuzzleSolver<>(controller);
            case A_STAR_SEQUENTIAL:
                return new AStarPuzzleSolver<>(controller);
            case A_STAR_CONCURRENT :
                return new AStarConcurrentPuzzleSolver<>(controller);
            case CONCURRENT_BREADTH :
                return new ConcurrentPuzzleSolver<>(controller, 0.4f);
            case CONCURRENT_DEPTH :
                return new ConcurrentPuzzleSolver<>(controller, 0.12f);
            case CONCURRENT_OPTIMUM :
                return new ConcurrentPuzzleSolver<>(controller, 0.2f);
            case GENETIC_SEARCH :
                return new GeneticSearchSolver(controller, false);
            case CONCURRENT_GENETIC_SEARCH :
                return new GeneticSearchSolver(controller, true);
        }
        return null;
    }

}
