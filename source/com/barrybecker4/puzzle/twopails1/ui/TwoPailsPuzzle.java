// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.twopails1.ui;

import com.barrybecker4.puzzle.common1.AlgorithmEnum;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common1.ui.DoneListener;
import com.barrybecker4.puzzle.common1.ui.NavigationPanel;
import com.barrybecker4.puzzle.common1.ui.PathNavigator;
import com.barrybecker4.puzzle.common1.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common1.ui.PuzzleViewer;
import com.barrybecker4.puzzle.twopails1.Algorithm;
import com.barrybecker4.puzzle.twopails1.TwoPailsPuzzleController;
import com.barrybecker4.puzzle.twopails1.model.Pails;
import com.barrybecker4.puzzle.twopails1.model.PourOperation;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JPanel;

/**
 * This was a puzzle described by Peter Norvig in his excellent "Design of Computer Programs" class on Udacity.
 * You start with two containers of size M and N liters. You need to measure out X liters using them.
 * What are the steps to do it?
 * For example you have two containers that hold 9 and 4 liters respectively. How do you measure out 6 liters?
 * See http://www.cut-the-knot.org/ctk/CartWater.shtml#solve
 */
public final class TwoPailsPuzzle extends PuzzleApplet<Pails, PourOperation>
                                  implements DoneListener {

    private NavigationPanel navPanel;

    /** Required to construct the applet. do not remove. */
    public TwoPailsPuzzle() {}

    /** Construct the application */
    private TwoPailsPuzzle(String[] args) {
        super(args);
    }

    @Override
    protected PuzzleViewer<Pails, PourOperation> createViewer() {
        return new TwoPailsViewer(this);
    }

    @Override
    protected PuzzleController<Pails, PourOperation> createController(Refreshable<Pails, PourOperation> viewer_) {
        return new TwoPailsPuzzleController(viewer_);
    }

    @Override
    protected AlgorithmEnum<Pails, PourOperation>[] getAlgorithmValues() {
        return Algorithm.values();
    }

    protected TopControls createTopControls() {
        return new TopControls(controller_, getAlgorithmValues());
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

        PuzzleApplet applet = new TwoPailsPuzzle(args);

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet);
    }

}

