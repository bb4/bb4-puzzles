// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common;

import com.barrybecker4.puzzle.common.solver.PuzzleSolver;

/**
 * Enum for type of solver to employ when solving the puzzle.
 * Solver for a given puzzle position P and state transition/move M.
 *
 * @author Barr Becker
 */
public interface AlgorithmEnum<P, M> {

    String getLabel();

    int ordinal();

    PuzzleSolver<M> createSolver(PuzzleController<P, M> controller);

}
