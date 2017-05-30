package com.barrybecker4.puzzle.common1.solver;

import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.model.PuzzleNode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solver that recognizes when no solution exists and stops running if that happens.
 *
 * @author Brian Goetz
 * @author Tim Peierls
 */
public class ConcurrentPuzzleSolver <P, M> extends BaseConcurrentPuzzleSolver<P, M> {

    private final AtomicInteger taskCount = new AtomicInteger(0);

    /**
     * @param puzzle the puzzle to solve
     * @param depthBreadthFactor the ratio of depth first to breadth first searching to use.
     *    May have significant performance impact. If 1, then all BFS, if 0 then all DFS.
     */
    public ConcurrentPuzzleSolver(PuzzleController<P, M> puzzle, float  depthBreadthFactor) {
        super(puzzle);
        setDepthBreadthFactor(depthBreadthFactor);
        taskCount.set(0);
    }


    @Override
    protected SolverTask newTask(P p, M m, PuzzleNode<P, M> n) {
        return new CountingSolverTask(p, m, n);
    }


    /**
     * Inner class to identify when all tasks have been run without finding a solution.
     */
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
                if (taskCount.decrementAndGet() == 0) {
                    // then there was no solution found
                    solution.setValue(null);
                }
            }
        }
    }
}
