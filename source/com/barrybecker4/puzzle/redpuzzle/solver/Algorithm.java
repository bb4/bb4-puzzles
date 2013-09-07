/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.redpuzzle.solver;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.solver.AStarPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.ConcurrentPuzzleSolver;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.common.solver.SequentialPuzzleSolver;
import com.barrybecker4.puzzle.redpuzzle.model.Piece;
import com.barrybecker4.puzzle.redpuzzle.model.PieceList;

/**
 * Enum for type of solver to employ when solving the puzzle.
 *
 * @author Barry Becker
 */
public enum Algorithm implements AlgorithmEnum<PieceList, Piece> {

    BRUTE_FORCE_ORIGINAL("Brute force (hand crafted)"),
    BRUTE_FORCE_SEQUENTIAL("Brute force (sequential)"),
    A_STAR_SEQUENTIAL("A* search (sequential)"),
    BRUTE_FORCE_CONCURRENT("Brute force (concurrent)"),
    BREADTH_FIRST_CONCURRENT("Mostly breath first concurrent"),
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
    public PuzzleSolver<Piece> createSolver(
            PuzzleController<PieceList, Piece> controller) {

        switch (this) {
            case BRUTE_FORCE_ORIGINAL :
                return new BruteForceSolver(controller);
            case BRUTE_FORCE_SEQUENTIAL :
                return new SequentialPuzzleSolver<>(controller);
            case A_STAR_SEQUENTIAL :
                return new AStarPuzzleSolver<>(controller);
            case BRUTE_FORCE_CONCURRENT :
                return new ConcurrentPuzzleSolver<>(controller, 0.2f);
            case BREADTH_FIRST_CONCURRENT :
                return new ConcurrentPuzzleSolver<>(controller, 0.1f);
            case GENETIC_SEARCH :
                return new GeneticSearchSolver(controller, false);
            case CONCURRENT_GENETIC_SEARCH :
                return new GeneticSearchSolver(controller, true);
        }
        return null; //never reached
    }
}
