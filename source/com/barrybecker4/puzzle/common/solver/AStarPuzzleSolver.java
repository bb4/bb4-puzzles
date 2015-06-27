package com.barrybecker4.puzzle.common.solver;

import com.barrybecker4.common.search.AStarSearch;
import com.barrybecker4.common.search.SearchSpace;

/**
 * @author Barry Becker
 */
public class AStarPuzzleSolver<P, M> extends AStarSearch<P, M> implements PuzzleSolver<M> {

    public AStarPuzzleSolver(SearchSpace<P, M> searchSpace) {
        super(searchSpace);
    }
}
