// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1;

import com.barrybecker4.common.concurrency.ThreadUtil;
import com.barrybecker4.puzzle.sudoku1.model.board.Board;
import com.barrybecker4.puzzle.sudoku1.model.update.IBoardUpdater;
import com.barrybecker4.puzzle.sudoku1.model.update.NonReflectiveBoardUpdater;
import com.barrybecker4.puzzle.sudoku1.model.update.ReflectiveBoardUpdater;

import java.awt.*;

/**
 * This does the hard work of actually solving the puzzle.
 * Controller in the model-view-controller pattern.
 *
 * @author Barry Becker
 */
public class SudokuSolver {

    private IBoardUpdater updater_;
    private int delay_;

    /**
     * Constructor
     */
    public SudokuSolver() {
        delay_ = 0;
        updater_ = new NonReflectiveBoardUpdater();
    }

    /** used to set custom updater if you want something other than the default */
    public void setUpdater(ReflectiveBoardUpdater updater) {
        updater_ = updater;
    }

    /**
     * Solves the puzzle.
     * This implements the main algorithm for solving the red puzzle.
     *
     * @return true if solved.
     */
    public boolean solvePuzzle(Board board) {
        return solvePuzzle(board, null);
    }

    void setDelay(int delay)  {
        delay_ = delay;
    }

    /**
     * Solves the puzzle.
     * This implements the main algorithm for solving the Sudoku puzzle.
     *
     * @param board puzzle to solve
     * @param puzzlePanel the viewer
     * @return true if solved.
     */
    public boolean solvePuzzle(Board board, Container puzzlePanel) {
        boolean solved;

        // not sure what this should be.
        int maxIterations = 2 * board.getEdgeLength();

        do {
            solved = doIteration(board);
            refreshWithDelay(puzzlePanel, 3);

        } while (!solved && board.getNumIterations() < maxIterations);

        refresh(puzzlePanel);

        // if we get here and solved is not true, we did not find a solution.
        return solved;
    }

    boolean doIteration(Board board)   {
        // find missing row and column numbers
        updater_.updateAndSet(board);
        board.incrementNumIterations();
        return board.solved();
    }

    private void refreshWithDelay(Container puzzlePanel, int relativeDelay) {
        if (delay_ >= 0)  {
            refresh(puzzlePanel);
            ThreadUtil.sleep(relativeDelay * delay_);
        }
    }

    private void refresh(Container puzzlePanel) {

        if (puzzlePanel == null || delay_ < 0)
            return;
        puzzlePanel.repaint();
        ThreadUtil.sleep(10 + delay_);  // give it a chance to repaint.
    }
}
