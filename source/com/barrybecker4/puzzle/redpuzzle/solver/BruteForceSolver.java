/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.redpuzzle.solver;

import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.redpuzzle.model.Piece;
import com.barrybecker4.puzzle.redpuzzle.model.PieceList;

import java.util.List;

/**
 * Works really well in spite of being brute force.
 * @see GeneticSearchSolver
 * for a potentially better alternative.
 *
 * Solves the puzzle in  10 seconds on Core2Duo sequentially.
 *
 * @author Barry Becker
 */
public class BruteForceSolver
       extends RedPuzzleSolver<PieceList, Piece> {


    public BruteForceSolver(PieceList pieces, Refreshable<PieceList, Piece> puzzlePanel) {
        super(pieces);
        puzzlePanel_ = puzzlePanel;
        assert (puzzlePanel_ != null): "for now we require a puzzle panel.";
        puzzlePanel_.refresh(pieces_, 0);
    }

    /**
     * @return true if a solution is found.
     */
    @Override
    public List<Piece> solve()  {
        List<Piece> moves = null;
        long startTime = System.currentTimeMillis();

        if  (solvePuzzle(puzzlePanel_, pieces_, 0).size() == 0) {
            moves = solution_.getPieces();
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        puzzlePanel_.finalRefresh(moves , solution_, numTries_, elapsedTime);

        return moves;
    }

    /**
     * Solves the puzzle.
     * This implements the main recursive algorithm for solving the red puzzle.
     * @param puzzlePanel will show the pieces as we arrange them.
     * @param pieces the pieces that have yet to be fitted.
     * @param i index of last placed piece. If we have to backtrack, we put it back where we got it.
     * @return true if successfully solved, false if no solution.
     */
    protected PieceList solvePuzzle( Refreshable<PieceList, Piece> puzzlePanel, PieceList pieces, int i ) {
        boolean solved = false;

        // base case of the recursion. If reached, the puzzle has been solved.
        if (pieces.size() == 0)
            return pieces;

        int k = 0;
        while (!solved && k < pieces.size() ) {
            Piece p = pieces.get(k);
            int r = 0;
            // try the 4 rotations
            while (!solved && r < 4) {
                 numTries_++;
                 if ( solution_.fits(p) ) {
                    solution_ = solution_.add( p );
                    pieces = pieces.remove( p );
                    puzzlePanel.refresh(solution_, numTries_);
                    puzzlePanel.makeSound();

                    // call solvePuzzle with a simpler case (one less piece to solve)
                    pieces = solvePuzzle( puzzlePanel, pieces, k);
                    solved = pieces.size() == 0;
                }
                if (!solved) {
                    p = p.rotate();
                }
                r++;
            }
            k++;
        }

        if (!solved && solution_.size() > 0) {
            // backtrack.
            Piece p = solution_.getLast();
            solution_ = solution_.removeLast();
            // put it back where we took it from,
            // so our list of unplaced pieces does not get out of order.
            pieces = pieces.add(i, p);
        }

        // if we get here and pieces is empty, we did not find a puzzlePanel.
        return pieces;
    }
}
