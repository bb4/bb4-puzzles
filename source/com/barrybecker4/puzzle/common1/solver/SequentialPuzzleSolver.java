package com.barrybecker4.puzzle.common1.solver;

import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.puzzle.common1.model.PuzzleNode;
import scala.Option;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import scala.collection.mutable.HashSet;
import scala.collection.mutable.Set;

/**
 * Naive Sequential puzzle solver.
 * Performs a depth first search on the state space.
 * It will find a solution if there is one, but it may not be the best solution or the shortest path to it.
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
    public Option<Seq<M>> solve() {
        P pos = puzzle.initialState();
        long startTime = System.currentTimeMillis();
        PuzzleNode<P, M> solutionState = search(new PuzzleNode<>(pos));

        Option<Seq<M>> pathToSolution = Option.empty();
        Option<P> solution = Option.empty();
        if (solutionState != null) {
            pathToSolution = Option.apply(solutionState.asMoveList());
            solution = Option.apply(solutionState.getPosition());
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
            //System.out.println("num seen = " + seen.size());
            if (puzzle.isGoal(currentState)) {
                return node;
            }
            Seq<M> moves = puzzle.legalTransitions(currentState);
            for (M move : JavaConversions.asJavaCollection(moves)) {
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
