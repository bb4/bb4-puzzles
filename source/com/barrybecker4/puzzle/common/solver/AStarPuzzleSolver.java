/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Sequential puzzle solver that uses the A* search algorithm.
 * See http://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * @author Barry Becker
 */
public class AStarPuzzleSolver<P, M> implements PuzzleSolver<P, M> {

    private final PuzzleController<P, M> puzzle;
    /** nodes that have been visited, but they may be replaced if we can reach them by a better path */
    private final Set<P> visited = new HashSet<P>();

    /** candidate nodes to search on the frontier. */
    private final PriorityQueue<PuzzleNode<P, M>> open;

    /** provides the value for the lowest cost path from the start node to the specified node (g score) */
    private final Map<P, Integer> pathCost = new HashMap<P, Integer>();

    private final Refreshable<P, M> ui;
    private long startTime;
    private long numTries = 0;

    /**
     * @param puzzle the puzzle to solve
     * @param ui the thing that can show its current state.
     */
    public AStarPuzzleSolver(PuzzleController<P, M> puzzle, Refreshable<P, M> ui) {
        this.puzzle = puzzle;
        this.ui = ui;
        open = new PriorityQueue<PuzzleNode<P, M>>(10);
    }

    @Override
    public List<M> solve() {
        P startingPos = puzzle.initialPosition();
        startTime =  System.currentTimeMillis();
        PuzzleNode<P, M> startNode =
                new PuzzleNode<P, M>(startingPos, null, null, puzzle.distanceFromGoal(startingPos));
        open.add(startNode);
        pathCost.put(startingPos, 0);

        List<M> pathToSolution = search();

        System.out.println((pathToSolution == null)?
                "No Solution found!" :
                "Number of steps in path to solution = " + pathToSolution.size());
        return pathToSolution;
    }

    /**
     * Depth first search for a solution to the puzzle.
     * @return list of moves leading to a solution. Null if no solution.
     */
    private List<M> search() {

        while (!open.isEmpty())  {
            PuzzleNode<P, M> currentNode = open.peek();
            P currentPosition = currentNode.getPosition();
            ui.refresh(currentPosition, numTries);

            if (puzzle.isGoal(currentPosition)) {
                List<M> path = currentNode.asMoveList();
                long elapsedTime = System.currentTimeMillis() - startTime;
                ui.finalRefresh(path, currentPosition, numTries, elapsedTime);
                return path;  // success
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
                            new PuzzleNode<P, M>(nbr, move, currentNode, estFutureCost);
                    pathCost.put(nbr, estPathCost);
                    if (!open.contains(child)) {
                        open.add(child);
                        numTries++;
                    }
                }
            }
        }
        return new LinkedList<M>();  // failure
    }
}
