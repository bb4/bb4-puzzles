// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver;

import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;


/**
 * Abstract base class for tantrix puzzle solving strategies (see strategy pattern).
 * Subclasses do the hard work of actually solving the puzzle.
 * This is the controller in the model-view-controller pattern.
 *
 * @author Barry Becker
 */
public abstract class TantrixSolver
                implements PuzzleSolver<TilePlacement> {

    protected TantrixBoard board;
    protected TantrixBoard solution_;

    /**
     * Constructor
     * @param board board with the unplaced pieces.
     */
    public TantrixSolver(TantrixBoard board) {
        this.board = board;
    }

    /**
     * Derived classes must provide the implementation for this abstract method.
     * @return true if a solution is found.
     */
    public abstract TilePlacementList solve();

    /**
     * @return the list of successfully placed pieces so far.
     */
    public TantrixBoard getSolution() {
        return solution_;
    }

    /**
     * @return the number of different ways we have tried to fit pieces together so far.
     */
    public long getNumIterations() {
        return 0;
    }

}
