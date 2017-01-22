// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.slidingpuzzle1.ui;

import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.ui.*;
import com.barrybecker4.puzzle.slidingpuzzle1.Algorithm;
import com.barrybecker4.puzzle.slidingpuzzle1.SlidingPuzzleController;
import com.barrybecker4.puzzle.slidingpuzzle1.model.SlideMove;
import com.barrybecker4.puzzle.slidingpuzzle1.model.SliderBoard;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.*;

/**
 * Sliding Puzzle - http://en.wikipedia.org/wiki/Sliding_puzzle.
 * This program solves a very difficult classic solitaire puzzle
 * where you slide tiles to form an image or correct sequence.
 */
public final class SlidingPuzzle extends PuzzleApplet<SliderBoard, SlideMove>
                             implements DoneListener {

    private NavigationPanel navPanel;

    /** Construct the applet */
    public SlidingPuzzle() {}

    /** Construct the application */
    SlidingPuzzle(String[] args) {
        super(args);
    }


    @Override
    protected PuzzleViewer<SliderBoard, SlideMove> createViewer() {
        return new SliderViewer(this);
    }

    @Override
    protected PuzzleController<SliderBoard, SlideMove> createController(Refreshable<SliderBoard, SlideMove> viewer_) {
        return new SlidingPuzzleController(viewer_);
    }

    @Override
    protected AlgorithmEnum<SliderBoard, SlideMove>[] getAlgorithmValues() {
        return Algorithm.values();
    }

    protected SliderTopControls createTopControls() {
        return new SliderTopControls(controller_, getAlgorithmValues());
    }

    @Override
    protected JPanel createBottomControls() {
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

