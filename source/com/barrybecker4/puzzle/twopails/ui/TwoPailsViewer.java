// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails.ui;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.puzzle.common.ui.DoneListener;
import com.barrybecker4.puzzle.common.ui.PathNavigator;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.twopails.model.Pails;
import com.barrybecker4.puzzle.twopails.model.PourOperation;

import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.util.List;

/**
 *  UI for drawing the current state of the puzzle.
 *  @author Barry Becker
 */
final class TwoPailsViewer extends PuzzleViewer<Pails, PourOperation>
                         implements PathNavigator {

    private TwoPailsRenderer renderer_ = new TwoPailsRenderer();
    private List<PourOperation> path_;
    private DoneListener doneListener;

    /**
     * Constructor.
     * @param listener called when the puzzle has been solved.
     */
    TwoPailsViewer(DoneListener listener) {
        doneListener = listener;
    }

    @Override
    public List<PourOperation> getPath() {
        return path_;
    }

    @Override
    public void refresh(Pails pails, long numTries) {
        board_ = pails;
        makeSound();
        status_ = createStatusMessage(numTries);
        simpleRefresh(pails, numTries);
    }

    @Override
    public void finalRefresh(List<PourOperation> path, Pails pails, long numTries, long millis) {

        super.finalRefresh(path, pails, numTries, millis);

        if (path == null)  {
            JOptionPane.showMessageDialog(this,
                AppContext.getLabel("NO_SOLUTION_FOUND"), AppContext.getLabel("NO_SOLUTION"), JOptionPane.WARNING_MESSAGE);
        }
        else {
            showPath(path, pails);
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
        if (board_ != null)
            renderer_.render(g, board_, getWidth(), getHeight());
    }

    public void showPath(List<PourOperation> path, Pails pails) {
        path_ = path;
        board_ = pails;
        if (doneListener != null) {
            doneListener.done();
        }
    }
}

