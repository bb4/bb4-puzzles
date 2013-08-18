/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.slidingpuzzle.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.puzzle.common.ui.DoneListener;
import com.barrybecker4.puzzle.common.ui.NavigationPanel;
import com.barrybecker4.puzzle.common.ui.PathNavigator;
import com.barrybecker4.puzzle.common.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.slidingpuzzle.Algorithm;
import com.barrybecker4.puzzle.slidingpuzzle.SlidingPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle.model.SlideMove;
import com.barrybecker4.puzzle.slidingpuzzle.model.Slider;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JPanel;

/**
 * Sliding Puzzle - http://en.wikipedia.org/wiki/Sliding_puzzle.
 * This program solves a very difficult classic solitaire puzzle
 * where you slide tiles to form an image or correct sequence.
 */
public final class SlidingPuzzle extends PuzzleApplet<Slider, SlideMove>
                             implements DoneListener {

    private NavigationPanel navPanel;

    /** Construct the applet */
    public SlidingPuzzle() {}

    /** Construct the application */
    public SlidingPuzzle(String[] args) {
        super(args);
    }


    @Override
    protected PuzzleViewer<Slider, SlideMove> createViewer() {
        return new SliderViewer(this);
    }

    @Override
    protected PuzzleController<Slider, SlideMove> createController(Refreshable<Slider, SlideMove> viewer_) {
        return new SlidingPuzzleController(viewer_);
    }

    @Override
    protected AlgorithmEnum<Slider, SlideMove>[] getAlgorithmValues() {
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

