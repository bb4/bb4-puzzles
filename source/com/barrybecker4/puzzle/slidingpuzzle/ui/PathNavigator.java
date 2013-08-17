// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.slidingpuzzle.model.Move;

import java.util.List;

/**
 * Allows navigating forward and backward in the solution path.
 * @author Barry Becker
 */
public interface PathNavigator {

    /**
     * @return the path to navigate on.
     */
    List<Move> getPath();

    /**
     * Switch from the current move in the sequence forwards or backwards stepSize.
     * @param currentPosition current position in path
     * @param stepSize num steps to move forward or backwards (depending on sign. e.g. -2 means two steps back).
     */
    void moveInPath(int currentPosition, int stepSize);

}
