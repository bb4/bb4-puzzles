/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import java.util.List;

/**
 * PuzzleSolver strategy pattern interface.
 * <p/>
 * P is the Puzzle type
 * M is the move type
 *
 * @author Barry Becker
 */
public interface PuzzleSolver<M>  {

    /**
     * Solve the puzzle and return a list of moves that lead to the solution.
     * @return list of moves (transitions) that can be made to arrive at a solution.
     *     Null if no solution found.
     */
    List<M> solve()  throws InterruptedException;
}
