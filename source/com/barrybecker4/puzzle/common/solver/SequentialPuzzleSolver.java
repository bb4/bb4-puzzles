/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Naive Sequential puzzle solver.
 * Performs a depth first search on the state space.
 * If will find a solution if there is one, but it may not be the best solution or the shortest path to it.
 * See A* for a better way to search that involves priority sorting of current paths.
 *
 * @author Brian Goetz, Tim Peierls  (Java Concurrency in Practice)
 * @author Barry Becker
 */
public class SequentialPuzzleSolver<P, M> implements PuzzleSolver<P, M> {

    private final PuzzleController<P, M> puzzle;
    private final Set<P> seen = new HashSet<P>();
    private final Refreshable<P, M> ui;
    private long numTries = 0;
    private long startTime;

    /**
     *
     * @param puzzle the puzzle to solve
     * @param ui the thing that can show its current state.
     */
    public SequentialPuzzleSolver(PuzzleController<P, M> puzzle, Refreshable<P, M> ui) {
        this.puzzle = puzzle;
        this.ui = ui;
    }

    @Override
    public List<M> solve() {
        P pos = puzzle.initialPosition();
        System.out.println("num seen = "+ seen.size());
        System.out.println("initial position=" + pos);
        startTime =  System.currentTimeMillis();
        List<M> pathToSolution = search(new PuzzleNode<P, M>(pos, null, null));

        System.out.println((pathToSolution == null)?
                "No Solution found!" :
                "Number of steps in path to solution = " + pathToSolution.size());
        return pathToSolution;
    }

    /**
     * Depth first search for a solution to the puzzle.
     * @param node the current state of the puzzle.
     * @return list of moves leading to a solution. Null if no solution.
     */
    private List<M> search(PuzzleNode<P, M> node) {
        P currentState = node.getPosition();
        if (!puzzle.alreadySeen(currentState, seen)) {
            if (puzzle.isGoal(currentState)) {
                List<M> path = node.asMoveList();

                long elapsedTime = System.currentTimeMillis() - startTime;
                ui.finalRefresh(path, currentState, numTries, elapsedTime);
                return path;
            }
            List<M> moves = puzzle.legalMoves(currentState);
            for (M move : moves) {
                P position = puzzle.move(currentState, move);

                if (ui != null) {
                    ui.refresh(position, numTries);
                }

                PuzzleNode<P, M> child = new PuzzleNode<P, M>(position, move, node);
                numTries++;
                List<M> result = search(child);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
