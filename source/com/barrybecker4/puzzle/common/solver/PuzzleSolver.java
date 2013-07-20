/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import java.util.List;

/**
 * PuzzleSolver interface
 * <p/>
 * P is the Puzzle type
 * M is the move type
 *
 * @author Barry Becker
 */
public interface PuzzleSolver<P, M>  {

    /**
     * Solve the puzzle and return a list of moves that lead to the solution.
     * @return list of moves (transitions) that can be made to arrive at a solution.
     */
    List<M> solve()  throws InterruptedException;
}
