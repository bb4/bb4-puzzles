/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sequential puzzle solver.
 * Performs a depth first search on the state space.
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
        System.out.println("initial position=" + pos);
        startTime =  System.currentTimeMillis();
        List<M> pathToSolution = search(new PuzzleNode<P, M>(pos, null, null));

        System.out.println((pathToSolution == null)?
                "No Solution" :
                "Number of steps in path to solution = " + pathToSolution.size());
        return pathToSolution;
    }

    private List<M> search(PuzzleNode<P, M> node) {
        if (!puzzle.alreadySeen(node.getPosition(), seen)) {
            if (puzzle.isGoal(node.getPosition())) {
                List<M> path = node.asMoveList();
                P position = node.getPosition();
                long elapsedTime = System.currentTimeMillis() - startTime;
                ui.finalRefresh(path, position, numTries, elapsedTime);
                return path;
            }
            List<M> moves = puzzle.legalMoves(node.getPosition());
            for (M move : moves) {
                P position = puzzle.move(node.getPosition(), move);

                // don't necessarily refresh every time as that would put too much load on the processor
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
