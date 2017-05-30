// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.common1.ui;


import com.barrybecker4.puzzle.common1.model.Move;

import java.util.List;

/**
 * Allows navigating forward and backward in the solution path.
 * @author Barry Becker
 */
public interface PathNavigator {

    /**
     * @return the path to navigate on.
     */
    List<? extends Move> getPath();

    /**
     * Switch from the current move in the sequence forwards or backwards stepSize.
     * @param currentStep current position in path
     * @param undo whether to make the move or undo it. For some puzzles applying the same move a second time undoes it.
     */
    void makeMove(int currentStep, boolean undo);

}
