// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.solver;

import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import scala.Option;
import scala.collection.Seq;


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
    TantrixBoard solution_;

    /**
     * Constructor
     * @param board board with the unplaced pieces.
     */
    TantrixSolver(TantrixBoard board) {
        this.board = board;
    }

    /**
     * Derived classes must provide the implementation for this abstract method.
     * @return true if a solution is found.
     */
    public abstract Option<Seq<TilePlacement>> solve();

    /**
     * @return the list of successfully placed pieces so far.
     */
    public TantrixBoard getSolution() {
        return solution_;
    }

}
