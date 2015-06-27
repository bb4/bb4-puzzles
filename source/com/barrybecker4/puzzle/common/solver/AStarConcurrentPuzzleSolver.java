package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.common.search.SearchSpace;

/**
 * @author Barry Becker
 */
public class AStarConcurrentPuzzleSolver<P, M> extends AStarPuzzleSolver<P, M> {

    public AStarConcurrentPuzzleSolver(SearchSpace<P, M> searchSpace) {
        super(searchSpace);
    }
}
