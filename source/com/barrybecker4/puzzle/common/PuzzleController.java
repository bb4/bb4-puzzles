/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common;

import java.util.List;
import java.util.Set;

/**
 * PuzzleController constructor.
 * <p/>
 * Abstraction for puzzles like the 'sliding blocks puzzle'
 * The type parameters P and M correspond to a position (state) and a move (transition from one state to the next).
 *
 * @author Brian Goetz, and Tim Peierls
 */
public interface PuzzleController<P, M> extends Refreshable<P, M> {

    P initialPosition();

    /**
     * @return true if the position is the goal state.
     */
    boolean isGoal(P position);

    /**
     * @return a list of legal next immutable moves.
     */
    List<M> legalMoves(P position);

    /**
     * @return the position (immutable) that you get after applying the specified move.
     */
    P move(P position, M move);

    /**
     * Add the position to the seen set of position if not already seen.
     *
     * @param position to check
     * @param seen Map of seen positions.
     * @return true if the specified position was already seen (possibly taking into account symmetries).
     */
    boolean alreadySeen(P position, Set<P> seen);

    /**
     * @return estimate of the cost to reach the goal from the specified position
     */
    int distanceFromGoal(P position);

    /**
     *specify the algorithm to use.
     */
    void setAlgorithm(AlgorithmEnum<P, M> algorithm);

    /**
     * Get the algorithm to use.
     * @return algorithm to use when solving.
     */
    AlgorithmEnum getAlgorithm();

    /**
     * Begin the search to find a solution to the puzzle.
     */
    void startSolving();
}
