/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;

import java.security.AccessControlException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ConcurrentPuzzleSolver
 * <p/>
 * Concurrent version of puzzle solver.
 * Does not recognize when there is no solution (use ConcurrentPuzzle Solver instead)
 *
 * @author Brian Goetz and Tim Peierls
 */
public class BaseConcurrentPuzzleSolver<P, M>  implements PuzzleSolver<P, M> {

    private static final int THREAD_POOL_SIZE = 100;

    private final PuzzleController<P, M> puzzle;
    private final ExecutorService exec;

    private final Set<P> seen;
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<PuzzleNode<P, M>>();
    private final Refreshable<P, M> ui;
    private volatile int numTries;
    /** default is a mixture between depth (0) (sequential) and breadth (1.0) (concurrent) first search. */
    private float depthBreadthFactor = 0.4f;
    private static final Random RANDOM = new Random(1);

    /**
     * Constructor
     * @param puzzle the puzzle instance to solve.
     * @param ui shows visible state
     */
    public BaseConcurrentPuzzleSolver(PuzzleController<P, M> puzzle, Refreshable<P, M> ui) {
        this.ui = ui;
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new HashSet<P>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    /**
     * The amount that you want the search to use depth first or breadth first search.
     * If factor is 0, then all depth first traversal and not concurrent, if 1 then all breadth first search and not sequential.
     * If the search is large, it is easier to run out of memory at the extremes.
     * Must be greater than 0 to have some amount of concurrency used.
     * @param factor a number between 0 and 1. One being all breadth first search and not sequential.
     */
    protected void setDepthBreadthFactor(float factor) {
        depthBreadthFactor = factor;
    }

    private ExecutorService initThreadPool() {
        //return Executors.newCachedThreadPool();
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Override
    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            long startTime = System.currentTimeMillis();
            exec.execute(newTask(p, null, null));
            // block until solution found
            PuzzleNode<P, M> solutionPuzzleNode = solution.getValue();
            List<M> path = (solutionPuzzleNode == null) ? null : solutionPuzzleNode.asMoveList();
            if (ui != null) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                P position = (solutionPuzzleNode == null) ? null : solutionPuzzleNode.getPosition();
                ui.finalRefresh(path, position, numTries, elapsedTime);
            }
            return path;
        } finally {
            try {
                exec.shutdown();
            } catch (AccessControlException e) {
                System.out.println("AccessControlException shutting down exec thread. " +
                        "Probably because running in a secure sandbox.");
            }
        }
    }

    protected SolverTask newTask(P p, M m, PuzzleNode<P, M> n) {
        return new SolverTask(p, m, n);
    }

    /**
     * Runnable used to solve a puzzle.
     */
    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
        SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        @Override
        public void run() {

            numTries++;
            if (solution.isSet() || puzzle.alreadySeen(getPosition(), seen)) {
                return; // already solved or seen this position
            }
            if (ui != null && !solution.isSet()) {

                ui.refresh(getPosition(), numTries);
            }
            if (puzzle.isGoal(getPosition())) {
                solution.setValue(this);
            }
            else {
                for (M move : puzzle.legalMoves(getPosition())) {
                    SolverTask task = newTask(puzzle.move(getPosition(), move), move, this);

                    // either process the children sequentially or concurrently based on  depthBreadthFactor
                    if (RANDOM.nextFloat() > depthBreadthFactor)
                        task.run();
                    else
                        exec.execute(task);
                }
            }
        }
    }
}

