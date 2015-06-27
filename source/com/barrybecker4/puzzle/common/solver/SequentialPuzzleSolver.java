package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.model.PuzzleNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Naive Sequential puzzle solver.
 * Performs a depth first search on the state space.
 * If will find a solution if there is one, but it may not be the best solution or the shortest path to it.
 * See A* for a better way to search that involves priority sorting of current paths.
 *
 * @author Brian Goetz
 * @author Tim Peierls  (Java Concurrency in Practice)
 * @author Barry Becker
 */
public class SequentialPuzzleSolver<P, M> implements PuzzleSolver<M> {

    private final PuzzleController<P, M> puzzle;

    /** The set of visited nodes. Do not re-search them. */
    private final Set<P> seen = new HashSet<>();
    private long numTries = 0;

    /**
     * @param puzzle the puzzle to solve
     */
    public SequentialPuzzleSolver(PuzzleController<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public List<M> solve() {
        P pos = puzzle.initialState();
        long startTime = System.currentTimeMillis();
        PuzzleNode<P, M> solutionState = search(new PuzzleNode<P, M>(pos));

        List<M> pathToSolution = null;
        P solution = null;
        if (solutionState != null) {
            pathToSolution = solutionState.asMoveList();
            solution = solutionState.getPosition();
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        puzzle.finalRefresh(pathToSolution, solution, numTries, elapsedTime);

        return pathToSolution;
    }

    /**
     * Depth first search for a solution to the puzzle.
     * @param node the current state of the puzzle.
     * @return list of moves leading to a solution. Null if no solution.
     */
    private PuzzleNode<P, M> search(PuzzleNode<P, M> node) {
        P currentState = node.getPosition();
        if (!puzzle.alreadySeen(currentState, seen)) {
            System.out.println("num seen = " + seen.size());
            if (puzzle.isGoal(currentState)) {
                return node;
            }
            List<M> moves = puzzle.legalTransitions(currentState);
            for (M move : moves) {
                P position = puzzle.transition(currentState, move);
                puzzle.refresh(position, numTries);

                PuzzleNode<P, M> child = new PuzzleNode<>(position, move, node);
                numTries++;
                PuzzleNode<P, M> result = search(child); // recursive call
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
