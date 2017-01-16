// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle1.solver;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.solver.AStarConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.ConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver;
import com.barrybecker4.puzzle.redpuzzle1.model.Piece;
import com.barrybecker4.puzzle.redpuzzle1.model.PieceList;

/**
 * Enum for type of solver to employ when solving the puzzle.
 *
 * @author Barry Becker
 */
public enum Algorithm implements AlgorithmEnum<PieceList, Piece> {

    BRUTE_FORCE_ORIGINAL,
    BRUTE_FORCE_SEQUENTIAL,
    A_STAR_SEQUENTIAL,
    A_STAR_CONCURRENT,
    CONCURRENT_DEPTH,
    CONCURRENT_BREADTH,
    GENETIC_SEARCH,
    CONCURRENT_GENETIC_SEARCH;

    private String label;

    /**
     *Private constructor
     * Creates a new instance of Algorithm1
     */
    Algorithm() {
        this.label = AppContext.getLabel(this.name());
    }

    public String getLabel() {
        return label;
    }

    /**
     * Create an instance of the algorithm given the controller and a refreshable.
     */
    public PuzzleSolver<Piece> createSolver(
            PuzzleController<PieceList, Piece> controller) {

        switch (this) {
            case BRUTE_FORCE_ORIGINAL :
                return new BruteForceSolver(controller);
            case BRUTE_FORCE_SEQUENTIAL :
                return new SequentialPuzzleSolver<>(controller);
            case A_STAR_SEQUENTIAL :
                return new AStarPuzzleSolver<>(controller);
            case A_STAR_CONCURRENT :
                return new AStarConcurrentPuzzleSolver<>(controller);
            case CONCURRENT_DEPTH :
                return new ConcurrentPuzzleSolver<>(controller, 0.1f);
            case CONCURRENT_BREADTH :
                return new ConcurrentPuzzleSolver<>(controller, 0.25f);
            case GENETIC_SEARCH :
                return new GeneticSearchSolver(controller, false);
            case CONCURRENT_GENETIC_SEARCH :
                return new GeneticSearchSolver(controller, true);
        }
        return null; //never reached
    }
}
