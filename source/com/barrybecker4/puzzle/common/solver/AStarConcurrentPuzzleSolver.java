/** Copyright by Barry G. Becker, 2012-2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.common.concurrency.Parallelizer;
import com.barrybecker4.common.concurrency.ThreadUtil;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.model.PuzzleNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Concurrent implementation of the A* search algorithm for solving puzzles.
 * See http://en.wikipedia.org/wiki/A*_search_algorithm
 * @author Barry Becker
 */
public class AStarConcurrentPuzzleSolver<P, M> extends AStarPuzzleSolver<P, M> {

    /** use the number of cores available as a default number of threads */
    private static final int NUM_WORKERS = Parallelizer.NUM_PROCESSORS;


    /**
     * @param puzzle the puzzle to solve
     */
    public AStarConcurrentPuzzleSolver(PuzzleController<P, M> puzzle) {
        this.puzzle = puzzle;
        visited = Collections.synchronizedSet(new HashSet<P>());
        openQueue = new PriorityBlockingQueue<>(20);
        pathCost = Collections.synchronizedMap(new HashMap<P, Integer>());
    }

    /**
     * Best first search for a solution to the puzzle.
     * @return the solution state node if found which has the path leading to a solution. Null if no solution.
     */
    protected PuzzleNode<P, M> doSearch() {

        Parallelizer parallelizer = new Parallelizer<>();

        List<Runnable> workers = new ArrayList<>(NUM_WORKERS);
        for (int i = 0; i < NUM_WORKERS; i++) {
            workers.add(new Worker(this));
        }
        // blocks until all Callables are done running.
        parallelizer.invokeAllRunnables(workers);

        return solution;
    }

    /**
     * Since this version is concurrent, initially we might ask for nodes off the queue
     * faster than they are added. That is the reason for the short sleep.
     * @return true if nodes are in the queue and not found a solution yet.
     */
    protected synchronized boolean nodesAvailable() {
        if (openQueue.isEmpty()) {
            ThreadUtil.sleep(10);
        }
        return !openQueue.isEmpty() && solution == null;
    }

    /** search worker */
    class Worker implements Runnable {
         AStarPuzzleSolver<P, M> solver;

        Worker(AStarPuzzleSolver<P, M> solver) {
            this.solver = solver;
        }

        @Override
        public void run() {
            PuzzleNode<P, M> sol = solver.search();
            if (sol != null)  {
                solution = sol;
            }
        }
    }
}
