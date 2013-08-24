// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.ConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;

/**
 * Enum for type of solver to employ when solving the puzzle.
 *
 * @author Barry Becker
 */
public enum Algorithm implements AlgorithmEnum<TantrixBoard, TilePlacement> {

    SEQUENTIAL("Solve sequentially (May run out of mem if >10)"),
    A_STAR("Solve sequentially using A* search"),
    CONCURRENT_BREADTH("Solve concurrently (mostly breadth first)"),
    CONCURRENT_DEPTH("Solve concurrently (mostly depth first)"),
    CONCURRENT_OPTIMUM("Solve concurrently (optimized between depth and breadth search)"),
    GENETIC_SEARCH("Genetic search"),
    CONCURRENT_GENETIC_SEARCH("Concurrent Genetic search");

    private String label;

    /**
     *Private constructor
     * Creates a new instance of Algorithm
     */
    Algorithm(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Create an instance of the algorithm given the controller and a refreshable.
     */
    public PuzzleSolver<TantrixBoard, TilePlacement> createSolver(PuzzleController<TantrixBoard, TilePlacement> controller,
                                                                  Refreshable<TantrixBoard, TilePlacement> ui) {

        switch (this) {
            case SEQUENTIAL :
                return new SequentialPuzzleSolver<TantrixBoard, TilePlacement>(controller, ui);
            case A_STAR :
                return new AStarPuzzleSolver<TantrixBoard, TilePlacement>(controller, ui);
            case CONCURRENT_BREADTH :
                return new ConcurrentPuzzleSolver<TantrixBoard, TilePlacement>(controller, 0.4f, ui);
            case CONCURRENT_DEPTH :
                return new ConcurrentPuzzleSolver<TantrixBoard, TilePlacement>(controller, 0.12f, ui);
            case CONCURRENT_OPTIMUM :
                return new ConcurrentPuzzleSolver<TantrixBoard, TilePlacement>(controller, 0.2f, ui);
            case GENETIC_SEARCH :
                return new GeneticSearchSolver(controller.initialPosition(), ui, false);
            case CONCURRENT_GENETIC_SEARCH :
                return new GeneticSearchSolver(controller.initialPosition(), ui, true);
        }
        return null;
    }

}
