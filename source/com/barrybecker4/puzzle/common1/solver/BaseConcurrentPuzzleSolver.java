package com.barrybecker4.puzzle.common1.solver;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.model.PuzzleNode;
import scala.Option;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import scala.collection.mutable.Set;
import scala.collection.mutable.HashSet;

import java.security.AccessControlException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Concurrent version of puzzle solver.
 * Does not recognize when there is no solution (use ConcurrentPuzzle Solver instead)
 * P is the Puzzle type
 * M is the move type
 *
 * @author Brian Goetz
 * @author Tim Peierls
 * @author Barry Becker
 */
public class BaseConcurrentPuzzleSolver<P, M>  implements PuzzleSolver<M> {

    private static final int THREAD_POOL_SIZE = 100;

    private final PuzzleController<P, M> puzzle;
    private final ExecutorService exec;

    /** Set of positions that have been visited */
    private final Set<P> seen;

    /** Prevents a value from being recieved until it is set. */
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<>();

    /** Number of nodes visited during search. Volatile to prevent corruption during concurrent updates */
    private volatile int numTries;

    /** default is a mixture between depth (0) (sequential) and breadth (1.0) (concurrent) first search. */
    private float depthBreadthFactor = 0.4f;

    /**
     * Constructor
     * @param puzzle the puzzle instance to solve.
     */
    public BaseConcurrentPuzzleSolver(PuzzleController<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new HashSet<>();
        numTries = 0;
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    /**
     * The amount that you want the search to use depth first or breadth first search.
     * If factor is 0, then all depth first traversal and no concurrent,
     * if 1, then all breadth first search and not sequential.
     * If the search is large, it is easier to run out of memory at the extremes.
     * Must be greater than 0 to have some amount of concurrency used.
     * @param factor a number between 0 and 1. One being all breadth first search and not sequential.
     */
    void setDepthBreadthFactor(float factor) {
        depthBreadthFactor = factor;
    }

    /** initialize the thread pool with some initial fixed size */
    private ExecutorService initThreadPool() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Override
    public Option<Seq<M>> solve() throws InterruptedException {
        try {
            return doSolve();
        } finally {
            try {
                exec.shutdown();
            } catch (AccessControlException e) {
                System.out.println("AccessControlException shutting down exec thread. " +
                        "Probably because running in a secure sandbox.");
            }
        }
    }

    /**
     * Solve the puzzle concurrently
     * @return list fo moves leading to the solution (assuming one was found).
     *  Null is returned if there was no solution.
     * @throws InterruptedException if interrupted during processing.
     */
    private Option<Seq<M>> doSolve() throws InterruptedException {
        P p = puzzle.initialState();
        long startTime = System.currentTimeMillis();
        exec.execute(newTask(p, null, null));

        // block until solution found
        PuzzleNode<P, M> solutionPuzzleNode = solution.getValue();

        // there has to be a better way to do this
        Option<Seq<M>> path = (solutionPuzzleNode == null) ?
                Option.apply(null) :
                Option.apply(solutionPuzzleNode.asMoveList());
        long elapsedTime = System.currentTimeMillis() - startTime;
        P position = (solutionPuzzleNode == null) ? null : solutionPuzzleNode.getPosition();
        System.out.println("solution = " + position);

        puzzle.finalRefresh(path, Option.apply(position), numTries, elapsedTime);

        return path;
    }

    protected SolverTask newTask(P p, M m, PuzzleNode<P, M> n) {
        return new SolverTask(p, m, n);
    }

    /**
     * Runnable used to solve a puzzle.
     * The {depthBreadthFactor} determines whether to process the children
     * sequentially or concurrently based on depthBreadthFactor.
     */
    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
        SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        @Override
        public void run() {

            numTries++;
            if (solution.isSet() || puzzle.alreadySeen(getPosition(), seen)) {
                return; // already solved or seen this position, so skip
            }
            puzzle.refresh(getPosition(), numTries);

            if (puzzle.isGoal(getPosition())) {
                solution.setValue(this);
            }
            else {
                Seq<M> transitions = puzzle.legalTransitions(getPosition());
                for (M move : JavaConversions.asJavaCollection(transitions) ) {
                    SolverTask task = newTask(puzzle.transition(getPosition(), move), move, this);

                    if (MathUtil.RANDOM.nextFloat() > depthBreadthFactor) {
                        // go deep
                        task.run();
                    } else {
                        // go wide
                        exec.execute(task);
                    }
                }
            }
        }
    }
}

