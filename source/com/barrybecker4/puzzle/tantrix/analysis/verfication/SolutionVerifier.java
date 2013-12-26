// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.analysis.verfication;

import com.barrybecker4.puzzle.tantrix.model.TantrixBoard;

/**
 * Used to determine whether or not a given tantrix state is a valid solution.
 *
 *  @author Barry Becker
 */
public class SolutionVerifier {

    TantrixBoard board;

    /**
     * Constructor.
     * @param board the tantrix state to test for solution.
     */
    public SolutionVerifier(TantrixBoard board) {
        this.board = board;
    }

    /**
     * The puzzle is solved if there is a loop of the primary color
     * and all secondary colors match. Since a tile can only be placed in
     * a valid position, we only need to check if there is a complete loop.
     * @return true if solved.
     */
    public boolean isSolved() {

        LoopDetector loopDetector = new LoopDetector(board);
        InnerSpaceDetector detector = new InnerSpaceDetector(board.getTantrix());

        return loopDetector.hasLoop() && !detector.hasInnerSpaces();
    }

}
