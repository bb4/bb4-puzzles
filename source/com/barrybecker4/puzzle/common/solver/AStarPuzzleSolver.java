/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.model.PuzzleNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Sequential puzzle solver that uses the A* search algorithm.
 * See http://en.wikipedia.org/wiki/A*_search_algorithm
 * A concurrent version of this algorithm could perhaps be made using PriorityBlockingQueue
 * @author Barry Becker
 */
public class AStarPuzzleSolver<P, M> implements PuzzleSolver<M> {

    private final PuzzleController<P, M> puzzle;

    /** nodes that have been visited, but they may be replaced if we can reach them by a better path */
    private final Set<P> visited = new HashSet<>();

    /** candidate nodes to search on the frontier. */
    private final Queue<PuzzleNode<P, M>> open = new PriorityQueue<>(10);

    /** provides the value for the lowest cost path from the start node to the specified node (g score) */
    private final Map<P, Integer> pathCost = new HashMap<>();

    private long numTries = 0;

    /**
     * @param puzzle the puzzle to solve
     */
    public AStarPuzzleSolver(PuzzleController<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public List<M> solve() {
        open.clear();
        visited.clear();
        pathCost.clear();

        P startingPos = puzzle.initialPosition();
        long startTime = System.currentTimeMillis();
        PuzzleNode<P, M> startNode =
                new PuzzleNode<>(startingPos, null, null, puzzle.distanceFromGoal(startingPos));
        open.add(startNode);
        pathCost.put(startingPos, 0);

        PuzzleNode<P, M> solutionState = search();

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
     * @return the solution state node if found which has the path leading to a solution. Null if no solution.
     */
    private PuzzleNode<P, M> search() {

        while (!open.isEmpty())  {
            PuzzleNode<P, M> currentNode = open.peek();
            P currentPosition = currentNode.getPosition();
            puzzle.refresh(currentPosition, numTries);

            if (puzzle.isGoal(currentPosition)) {
                return currentNode;  // success
            }
            visited.add(open.remove().getPosition());
            List<M> moves = puzzle.legalMoves(currentPosition);
            for (M move : moves) {
                P nbr = puzzle.move(currentPosition, move);
                // for now, assume the distance to all nbrs from the current position is 1
                int estPathCost = pathCost.get(currentPosition) + 1;
                if (!visited.contains(nbr) || estPathCost < pathCost.get(nbr)) {
                    int estFutureCost = estPathCost + puzzle.distanceFromGoal(nbr);
                    PuzzleNode<P, M> child =
                            new PuzzleNode<>(nbr, move, currentNode, estFutureCost);
                    pathCost.put(nbr, estPathCost);
                    if (!open.contains(child)) {
                        open.add(child);
                        numTries++;
                    }
                }
            }
        }
        return null;  // failure
    }
}
