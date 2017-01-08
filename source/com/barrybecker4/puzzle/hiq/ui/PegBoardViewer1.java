// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.hiq.ui;

import com.barrybecker4.puzzle.common.ui.DoneListener;
import com.barrybecker4.puzzle.common.ui.PathNavigator;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.hiq.model.PegBoard1;
import com.barrybecker4.puzzle.hiq.model.PegMove1;

import java.awt.Graphics;
import java.util.List;

/**
 *  UI for drawing the current best solution to the puzzle.
 *  @author Barry Becker
 */
final class PegBoardViewer1 extends PuzzleViewer<PegBoard1, PegMove1>
                           implements PathNavigator {

    private PegBoardRenderer1 renderer_ = new PegBoardRenderer1();
    private List<PegMove1> path_;
    private DoneListener doneListener;

    /**
     * Constructor.
     */
    PegBoardViewer1(PegBoard1 board, DoneListener listener) {
        doneListener = listener;
        board_ = board;
    }

    @Override
    public List<PegMove1> getPath() {
        return path_;
    }

    @Override
    public void refresh(PegBoard1 board, long numTries) {
        if (numTries % 4000 == 0) {
            status_ = createStatusMessage(numTries);
            simpleRefresh(board, numTries);
        }
    }

    @Override
    public void finalRefresh(List<PegMove1> path, PegBoard1 board, long numTries, long millis) {
        super.finalRefresh(path, board, numTries, millis);
        if (board != null)  {
            makeSound();
            showPath(path, board);
        }
    }

    @Override
    public void makeMove(int currentStep, boolean undo) {
        board_ = board_.doMove(getPath().get(currentStep), undo);
        repaint();
    }

    /**
     * This renders the current state of the puzzle to the screen.
     */
    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        renderer_.render(g, board_, getWidth(), getHeight());
    }

    public void showPath(List<PegMove1> path, PegBoard1 board) {

        path_ = path;
        board_ = board;
        System.out.println("path size="+ path.size());  // NON-NLS
        if (doneListener != null) {
            doneListener.done();
        }
    }
}

