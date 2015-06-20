/** Copyright by Barry G. Becker, 2012-2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
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
 * A concurrent version of this algorithm could be made using a PriorityBlockingQueue for {@code openQueue}
 * @author Barry Becker
 */
public class AStarPuzzleSolver<P, M> implements PuzzleSolver<M> {

    protected PuzzleController<P, M> puzzle;

    /** nodes that have been visited, but they may be replaced if we can reach them by a better path */
    protected Set<P> visited;

    /** candidate nodes to search on the frontier. */
    protected Queue<PuzzleNode<P, M>> openQueue;

    /** provides the value for the lowest cost path from the start node to the specified node (g score) */
    protected Map<P, Integer> pathCost;

    protected volatile PuzzleNode<P, M> solution;

    /** number of steps to find solution */
    protected long numTries;

    /**
     * @param puzzle the puzzle to solve
     */
    public AStarPuzzleSolver(PuzzleController<P, M> puzzle) {
        this.puzzle = puzzle;
        visited = new HashSet<>();
        openQueue = new PriorityQueue<>(20);
        pathCost = new HashMap<>();
    }

    protected AStarPuzzleSolver() {}

    @Override
    public List<M> solve() {

        P startingPos = puzzle.initialPosition();
        long startTime = System.currentTimeMillis();
        PuzzleNode<P, M> startNode =
                new PuzzleNode<>(startingPos, puzzle.distanceFromGoal(startingPos));
        openQueue.add(startNode);
        pathCost.put(startingPos, 0);

        PuzzleNode<P, M> solutionState = doSearch();

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
     * Best first search for a solution to the puzzle.
     * @return the solution state node if found which has the path leading to a solution. Null if no solution.
     */
    protected PuzzleNode<P, M> doSearch() {
        return search();
    }

    /**
     * Best first search for a solution to the puzzle.
     * @return the solution state node if found which has the path leading to a solution. Null if no solution.
     */
    protected PuzzleNode<P, M> search() {

        while (nodesAvailable())  {
            PuzzleNode<P, M> currentNode = openQueue.remove();
            P currentPosition = currentNode.getPosition();
            puzzle.refresh(currentPosition, numTries);

            if (puzzle.isGoal(currentPosition)) {
                solution = currentNode;
                return currentNode;  // success
            }
            visited.add(currentPosition);
            List<M> moves = puzzle.legalMoves(currentPosition);
            for (M move : moves) {
                P nbr = puzzle.move(currentPosition, move);
                int estPathCost = pathCost.get(currentPosition) + puzzle.getCost(move);
                if (!visited.contains(nbr) || estPathCost < pathCost.get(nbr)) {
                    int estFutureCost = estPathCost + puzzle.distanceFromGoal(nbr);
                    PuzzleNode<P, M> child =
                            new PuzzleNode<>(nbr, move, currentNode, estFutureCost);
                    pathCost.put(nbr, estPathCost);
                    if (!openQueue.contains(child)) {
                        openQueue.add(child);
                        numTries++;
                    }
                }
            }
        }
        return null;  // failure
    }

    protected boolean nodesAvailable() {
        return !openQueue.isEmpty();
    }
}
