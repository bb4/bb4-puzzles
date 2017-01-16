// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.redpuzzle1.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.redpuzzle1.model.Piece;
import com.barrybecker4.puzzle.redpuzzle1.model.PieceList;
import scala.Option;
import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * Works really well in spite of being brute force.
 * Solves the puzzle in 10 seconds on Core2Duo sequentially.
 *
 * @author Barry Becker
 */
public class BruteForceSolver
       extends RedPuzzleSolver {


    BruteForceSolver(PuzzleController<PieceList, Piece> puzzle) {
        super(puzzle);

        puzzle.refresh(pieces_, 0);
    }

    /**
     * @return true if a solution is found.
     */
    @Override
    public Option<Seq<Piece>> solve()  {
        Option<Seq<Piece>> moves = Option.empty();
        long startTime = System.currentTimeMillis();

        if  (solvePuzzle(pieces_, 0).size() == 0) {
            moves = Option.apply(JavaConversions.asScalaBuffer(solution_.getPieces()).toSeq());
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        puzzle.finalRefresh(moves , Option.apply(solution_), numTries_, elapsedTime);

        return moves;
    }

    /**
     * Solves the puzzle.
     * This implements the main recursive algorithm for solving the red puzzle.
     * @param pieces the pieces that have yet to be fitted.
     * @param i index of last placed piece. If we have to backtrack, we put it back where we got it.
     * @return true if successfully solved, false if no solution.
     */
    private PieceList solvePuzzle(PieceList pieces, int i ) {
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
                    puzzle.refresh(solution_, numTries_);

                    // call solvePuzzle with a simpler case (one less piece to solve)
                    pieces = solvePuzzle(pieces, k);
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
            // put it back where we took it from, so the list of unplaced pieces is still in order.
            pieces = pieces.add(i, p);
        }

        // if we get here and pieces is empty, we did not find a solution.
        return pieces;
    }
}
