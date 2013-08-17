/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.slidingpuzzle.model.Board;
import com.barrybecker4.puzzle.slidingpuzzle.model.Move;

import java.awt.Graphics;
import java.util.List;

/**
 *  UI for drawing the current best solution to the puzzle.
 *  @author Barry Becker
 */
final class BoardViewer extends PuzzleViewer<Board, Move>
                           implements PathNavigator {

    private BoardRenderer renderer_ = new BoardRenderer();
    private List<Move> path_;
    private DoneListener doneListener;

    /**
     * Constructor.
     */
    BoardViewer(DoneListener listener) {
        doneListener = listener;
    }

    @Override
    public List<Move> getPath() {
        return path_;
    }

    @Override
    public void refresh(Board board, long numTries) {
        board_ = board;
        if (numTries % 6000 == 0) {
            status_ = createStatusMessage(numTries);
            simpleRefresh(board, numTries);
        }
    }

    @Override
    public void finalRefresh(List<Move> path, Board board, long numTries, long millis) {
        super.finalRefresh(path, board, numTries, millis);
        if (board != null)  {
            showPath(path, board);
        }
    }

    @Override
    public void makeSound() {
        // add sound
    }


    @Override
    public void moveInPath(int currentPosition, int stepSize) {
        int currentStep = currentPosition;
        int inc = stepSize > 0 ? 1 : -1;
        int toStep = currentStep + stepSize;
        do {
            makeMove(currentStep);
            currentStep += inc;
        } while (currentStep != toStep);
        repaint();
    }

    public void makeMove(int currentStep) {
        board_ = board_.doMove(getPath().get(currentStep));
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

    public void showPath(List<Move> path, Board board) {

        path_ = path;
        board_ = board;
        System.out.println("path size="+ path.size());  // NON-NLS
        if (doneListener != null) {
            doneListener.done();
        }
    }
}

