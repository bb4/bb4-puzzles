// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix1.ui;

import com.barrybecker4.puzzle.common1.ui.PuzzleViewer;
import com.barrybecker4.puzzle.tantrix1.model.TantrixBoard;
import com.barrybecker4.puzzle.tantrix1.model.TilePlacement;
import com.barrybecker4.puzzle.tantrix1.ui.rendering.TantrixBoardRenderer;

import java.awt.*;

/**
 * Draws the current best solution to the puzzle in a panel.
 * The view in the model-view-controller pattern.
 *
 * @author Barry Becker
 */
public final class TantrixViewer extends PuzzleViewer<TantrixBoard, TilePlacement> {

    private TantrixBoardRenderer renderer_;

    /**
     * Constructor.
     */
    public TantrixViewer() {
        renderer_ = new TantrixBoardRenderer();
    }

    public TantrixBoard getBoard() {
        return board_;
    }

    /**
     *  This renders the current state of the PuzzlePanel to the screen.
     *  This method is part of the component interface.
     */
    @Override
    protected void paintComponent( Graphics g ) {

        super.paintComponent(g);
        renderer_.render(g, board_, getWidth(), getHeight());
    }


    @Override
    public void refresh(TantrixBoard board, long numTries) {
        //int rate = board_ == null ? 1 : board_.getNumTiles()-2;
        //System.out.println("rate=" + rate + " numTries="+ numTries+" numTries % rate=" + numTries % rate);
        //if (numTries % rate == 0) {
        status_ = createStatusMessage(numTries);
        simpleRefresh(board, numTries);
        //ThreadUtil.sleep(100);
        //}
    }

    public void makeSound() {
        int note = Math.min(127, 20 + getBoard().getUnplacedTiles().size() * 12);
        musicMaker_.playNote(note * 20, 20, 640);
    }
}

