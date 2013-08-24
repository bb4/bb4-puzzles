/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.redpuzzle;

import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.AbstractPuzzleController;
import com.barrybecker4.puzzle.redpuzzle.model.Piece;
import com.barrybecker4.puzzle.redpuzzle.model.PieceList;
import com.barrybecker4.puzzle.redpuzzle.solver.Algorithm;

import java.util.LinkedList;
import java.util.List;
import static com.barrybecker4.puzzle.redpuzzle.model.PieceList.NUM_PIECES;

/**
 * The controller allows the solver to do its thing by providing the PuzzleController api.
 * Originally I had implemented solvers without trying to do concurrency, and those less generic
 * forms still exist, but do not require the PuzzleController api.
 *
 * The generic solvers (sequential and concurrent) expect the PieceList to represent the state of a board,
 * and the Piece to represent a move. The way a move is applied is simply to add the piece to the
 * end of the current list.
 *
 * Created on August 11, 2007
 * @author Barry Becker
 */
public class RedPuzzleController extends AbstractPuzzleController<PieceList, Piece> {

    private final PieceList SHUFFLED_PIECES = PieceList.getInitialPuzzlePieces();

    /**
     * Creates a new instance of RedPuzzleController
     */
    public RedPuzzleController(Refreshable<PieceList, Piece> ui) {
        super(ui);
        algorithm_ = Algorithm.BRUTE_FORCE_ORIGINAL;
    }

    @Override
    public PieceList initialPosition() {
        // empty piece list
        return new PieceList();
    }

    @Override
    public boolean isGoal(PieceList position) {
        // we have reached our goal if we have 9 pieces that fit
        return (position.size() == NUM_PIECES);
    }

    /**
     * The simplest estimate of the cost to reach the goal is 9 - number of pieces placed so far.
     * @return estimate of the cost to reach the goal of all 9 pieces successfully placed
     */
    public int distanceFromGoal(PieceList position) {
        return NUM_PIECES - position.size();
    }


    /**
     * For each piece that we have not tried yet, see if it fits.
     * If it does, add that to the set of legal next moves.
     * @param position position to look from.
     * @return list of legal moves that can be made from current position.
     */
    @Override
    public List<Piece> legalMoves(PieceList position) {

        List<Piece> moves = new LinkedList<Piece>();
        for  (int i = 0; i < NUM_PIECES; i++) {
            Piece p = SHUFFLED_PIECES.get(i);
            if (!position.contains(p)) {
                int r = 0;
                // see if any of the rotations fit.
                while (!position.fits(p) && r < 4) {
                    p = p.rotate();
                    r++;
                }
                if (r < 4) {
                    moves.add(p);
                }
            }
        }
        return moves;
    }

    @Override
    public PieceList move(PieceList position, Piece move) {
        // To make a move, simple add the piece to the end of our list
        assert position.fits(move) : move  +" does not fit in  "+position;
        return position.add(move);
    }

}
