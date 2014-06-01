/** Copyright by Barry G. Becker, 2012-2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.common.concurrency.ThreadUtil;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.model.PuzzleNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Concurrent implementation of the A* search algorithm for solving puzzles.
 * See http://en.wikipedia.org/wiki/A*_search_algorithm
 * @author Barry Becker
 */
public class AStarConcurrentPuzzleSolver<P, M> implements PuzzleSolver<M> {

    /** only add a new thread every 10th time */
    private static final double PROB_NEW_THREAD_INC = 1.0;

    private final PuzzleController<P, M> puzzle;

    /**
     * Nodes that have been visited, but they may be replaced if we can reach them by a better path.
     * This collection must have synchronized access.
     */
    private final Set<P> visited = Collections.synchronizedSet(new HashSet<P>());

    /** Candidate nodes to search on the frontier. */
    private final Queue<PuzzleNode<P, M>> open = new PriorityBlockingQueue<>(20);

    /**
     * Provides the value for the lowest cost path from the start node to the specified node (g score).
     * Must have synchronized access.
     */
    private final Map<P, Integer> pathCost = Collections.synchronizedMap(new HashMap<P, Integer>());

    private volatile long numTries = 0;

    /**
     * @param puzzle the puzzle to solve
     */
    public AStarConcurrentPuzzleSolver(PuzzleController<P, M> puzzle) {
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
                new PuzzleNode<>(startingPos, puzzle.distanceFromGoal(startingPos));
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
     * Best first search for a solution to the puzzle.
     * @return the solution state node if found which has the path leading to a solution. Null if no solution.
     */
    private PuzzleNode<P, M> search() {

        while (nodesAvailable())  {
            PuzzleNode<P, M> currentNode = open.remove();
            P currentPosition = currentNode.getPosition();
            puzzle.refresh(currentPosition, numTries);

            if (puzzle.isGoal(currentPosition)) {
                return currentNode;  // success
            }
            visited.add(currentPosition);

            if (MathUtil.RANDOM.nextDouble() < PROB_NEW_THREAD_INC) {
                new Thread(new Worker(currentPosition, currentNode)).start();
            }
            else {
                new Worker(currentPosition, currentNode).run();
            }

        }
        return null;  // failure
    }

    private boolean nodesAvailable() {
        if (open.isEmpty()) {
            ThreadUtil.sleep(10);
        }
        return !open.isEmpty();
    }


    class Worker implements Runnable {

        P currentPosition;
        PuzzleNode<P, M> currentNode;

        Worker(P pos, PuzzleNode<P, M> node) {
            currentPosition = pos;
            currentNode = node;
        }

        public void run() {
            List<M> moves = puzzle.legalMoves(currentPosition);
            for (M move : moves) {
                P nbr = puzzle.move(currentPosition, move);
                int estPathCost = pathCost.get(currentPosition) + puzzle.getCost(move);
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
    }
}
