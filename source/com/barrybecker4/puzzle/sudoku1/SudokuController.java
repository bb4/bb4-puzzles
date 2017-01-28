// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku1;

import com.barrybecker4.common.concurrency.Worker;
import com.barrybecker4.puzzle.sudoku1.ui.SudokuPanel;

import java.awt.Cursor;


/**
 * Controller part of the MVC pattern.
 *
 * @author Barry Becker
 */
public final class SudokuController {

    private SudokuPanel puzzlePanel_;

    /**
     * Construct the application and set the look and feel.
     */
    public SudokuController(SudokuPanel panel) {
        puzzlePanel_ = panel;
    }

    public void setShowCandidates(boolean show) {
        puzzlePanel_.setShowCandidates(show);
    }

    public void generatePuzzle(final int delay, final int size) {

        Worker worker = new Worker() {

            @Override
            public Object construct() {
                puzzlePanel_.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                SudokuGenerator generator = new SudokuGenerator(size, puzzlePanel_);
                generator.setDelay(delay);
                puzzlePanel_.generateNewPuzzle(generator);
                return true;
            }

            @Override
            public void finished() {
                puzzlePanel_.repaint();
                puzzlePanel_.setCursor(Cursor.getDefaultCursor());
            }
        };
        worker.start();
    }

    public void solvePuzzle(final int delay) {

        Worker worker = new Worker() {

            @Override
            public Object construct() {
                SudokuSolver solver = new SudokuSolver();
                solver.setDelay(delay);
                puzzlePanel_.startSolving(solver);
                return true;
            }

            @Override
            public void finished() {
                puzzlePanel_.repaint();
            }
        };
        worker.start();
    }

    public void validatePuzzle() {
        puzzlePanel_.validatePuzzle();
    }
}
