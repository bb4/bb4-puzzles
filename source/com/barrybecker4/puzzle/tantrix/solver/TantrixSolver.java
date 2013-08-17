// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.solver;

import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix.model.TilePlacementList;


/**
 * Abstract base class for puzzle solver strategies (see strategy pattern).
 * Subclasses do the hard work of actually solving the puzzle.
 * Controller in the model-view-controller pattern.
 *
 * @author Barry Becker
 */
public abstract class TantrixSolver<P, K>
                implements PuzzleSolver<TantrixBoard, TilePlacement> {

    protected TantrixBoard board;
    protected TantrixBoard solution_;

    /** some measure of the number of iterations the solver needs to solve the puzzle. */
    private long numTries_ = 0;

    protected Refreshable<TantrixBoard, TilePlacement> puzzlePanel_;

    /**
     * Constructor
     * @param board board with the unplaced pieces.
     */
    public TantrixSolver(TantrixBoard board) {
        this.board = board;
        //this.solution_ = new TilePlacementList();
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
        return numTries_;
    }

}
