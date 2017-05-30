package com.barrybecker4.puzzle.common1.solver;

import com.barrybecker4.search.AStarSearch;
import com.barrybecker4.search.HeapPriorityQueue;
import com.barrybecker4.search.SearchSpace;

/**
 * @author Barry Becker
 */
public class AStarPuzzleSolver<P, M> extends AStarSearch<P, M> implements PuzzleSolver<M> {

    public AStarPuzzleSolver(SearchSpace<P, M> searchSpace) {
        super(searchSpace, new HeapPriorityQueue<P, M>(256, null));
    }
}
