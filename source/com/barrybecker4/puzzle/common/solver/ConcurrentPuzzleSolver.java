/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * PuzzleSolver
 * <p/>
 * Solver that recognizes when no solution exists and stops running if that happens.
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ConcurrentPuzzleSolver <P, M> extends BaseConcurrentPuzzleSolver<P, M> {

    public ConcurrentPuzzleSolver(PuzzleController<P, M> puzzle, Refreshable<P, M> ui) {
        super(puzzle, ui);
    }

    /**
     * @param puzzle the puzzle to solve
     * @param depthBreadthFactor the ratio of depth first to breadth first searching to use. May have significant performance impact.
     * @param ui refreshable something that can show the current state visually.
     */
    public ConcurrentPuzzleSolver(PuzzleController<P, M> puzzle, float  depthBreadthFactor, Refreshable<P, M> ui) {
        super(puzzle, ui);
        setDepthBreadthFactor(depthBreadthFactor);
    }

    private final AtomicInteger taskCount = new AtomicInteger(0);

    @Override
    protected SolverTask newTask(P p, M m, PuzzleNode<P, M> n) {
        return new CountingSolverTask(p, m, n);
    }

    class CountingSolverTask extends SolverTask {
        CountingSolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0)
                    solution.setValue(null);
            }
        }
    }
}
