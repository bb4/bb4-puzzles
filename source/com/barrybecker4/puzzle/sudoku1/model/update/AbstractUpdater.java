// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1.model.update;

import com.barrybecker4.puzzle.sudoku1.model.board.Board;

/**
 *  The Slider describes the physical layout of the puzzle.
 *
 *  @author Barry Becker
 */
public abstract class AbstractUpdater implements IUpdater {

    /** the sudoku board   */
    protected Board board;

    /**
     * Constructor
     * @param board the board to update
     */
    public AbstractUpdater(Board board) {
        this.board = board;
    }

    /**
     * {@inheritDoc}
     */
    public abstract void updateAndSet();

}
