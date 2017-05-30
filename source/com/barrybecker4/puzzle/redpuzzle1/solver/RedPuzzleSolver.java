// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle1.solver;

import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.solver.PuzzleSolver;
import com.barrybecker4.puzzle.redpuzzle1.model.Piece;
import com.barrybecker4.puzzle.redpuzzle1.model.PieceList;
import com.barrybecker4.puzzle.redpuzzle1.model.PieceLists;
import scala.Option;
import scala.collection.Seq;


/**
 * Abstract base class for puzzle solver strategies (see strategy pattern).
 * Subclasses do the hard work of actually solving the puzzle.
 * Controller in the model-view-controller pattern.
 *
 * @author Barry Becker
 */
public abstract class RedPuzzleSolver
                implements PuzzleSolver<Piece> {

    /** the unsorted pieces that we draw from and place in the solvedPieces list. */
    PieceList pieces_;

    /** the pieces we have correctly fitted so far. */
    PieceList solution_;

    /** some measure of the number of iterations the solver needs to solve the puzzle. */
    int numTries_ = 0;

    PuzzleController<PieceList, Piece> puzzle;

    /**
     * Constructor
     * @param puzzle the puzzle to solve.
     */
    RedPuzzleSolver(PuzzleController<PieceList, Piece> puzzle) {
        this.puzzle = puzzle;
        pieces_ = PieceLists.getInitialPuzzlePieces();
        solution_ = new PieceList();
    }

    /**
     * Derived classes must provide the implementation for this abstract method.
     * @return true if a solution is found.
     */
    @Override
    public abstract Option<Seq<Piece>> solve();

}
