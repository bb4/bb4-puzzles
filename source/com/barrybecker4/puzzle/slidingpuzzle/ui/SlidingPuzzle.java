/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.slidingpuzzle.Algorithm;
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle.model.Slider;
import com.barrybecker4.puzzle.slidingpuzzle.model.Move;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JPanel;

/**
 * Sliding Puzzle - http://en.wikipedia.org/wiki/Sliding_puzzle.
 * This program solves a very difficult classic solitaire puzzle
 * where you slide tiles to form an image or correct sequence.
 */
public final class SlidingPuzzle extends PuzzleApplet<Slider, Move>
                             implements DoneListener {

    private NavigationPanel navPanel;

    /** Construct the applet */
    public SlidingPuzzle() {}

    /** Construct the application */
    public SlidingPuzzle(String[] args) {
        super(args);
    }


    @Override
    protected PuzzleViewer<Slider, Move> createViewer() {
        return new BoardViewer(this);
    }

    @Override
    protected PuzzleController<Slider, Move> createController(Refreshable<Slider, Move> viewer_) {
        return new SlidingPuzzleController(viewer_);
    }

    @Override
    protected AlgorithmEnum<Slider, Move>[] getAlgorithmValues() {
        return Algorithm.values();
    }

    @Override
    protected JPanel createCustomControls() {
        navPanel = new NavigationPanel();
        return navPanel;
    }

    @Override
    public void done() {
        navPanel.setPathNavigator((PathNavigator) viewer_);
    }

    /**
     * Use this to run as an application instead of an applet.
     */
    public static void main(String[] args) {

        PuzzleApplet applet = new SlidingPuzzle(args);

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet);
    }

}

