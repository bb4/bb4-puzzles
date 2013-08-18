/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.common.ui.DoneListener;
import com.barrybecker4.puzzle.common.ui.PathNavigator;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove;
import com.barrybecker4.puzzle.slidingpuzzle.model.Slider;
import com.barrybecker4.sound.MusicMaker;

import java.awt.Graphics;
import java.util.List;

/**
 *  UI for drawing the current best solution to the puzzle.
 *  @author Barry Becker
 */
final class SliderViewer extends PuzzleViewer<Slider, SlideMove>
                         implements PathNavigator {

    private SliderRenderer renderer_ = new SliderRenderer();
    private List<SlideMove> path_;
    private DoneListener doneListener;

    /** play a sound effect when a piece goes into place. */
    private MusicMaker musicMaker_ = new MusicMaker();

    /**
     * Constructor.
     * @param listener called when the puzzle has been solved.
     */
    SliderViewer(DoneListener listener) {
        doneListener = listener;
    }

    @Override
    public List<SlideMove> getPath() {
        return path_;
    }

    @Override
    public void refresh(Slider board, long numTries) {
        board_ = board;
        if (numTries % 500 == 0) {
            makeSound();
            status_ = createStatusMessage(numTries);
            simpleRefresh(board, numTries);
        }
    }

    @Override
    public void finalRefresh(List<SlideMove> path, Slider board, long numTries, long millis) {
        super.finalRefresh(path, board, numTries, millis);
        if (board != null)  {
            showPath(path, board);
        }
    }

    /**
     * make a little click noise when the piece fits into place.
     */
    public void makeSound() {
        musicMaker_.playNote(60, 5, 940);
    }

    @Override
    public void makeMove(int currentStep, boolean undo) {
        board_ = board_.doMove(getPath().get(currentStep));
        repaint();
    }

    /**
     * This renders the current state of the puzzle to the screen.
     */
    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        if (board_ != null)
            renderer_.render(g, board_, getWidth(), getHeight());
    }

    public void showPath(List<SlideMove> path, Slider board) {
        path_ = path;
        board_ = board;
        if (doneListener != null) {
            doneListener.done();
        }
    }
}

