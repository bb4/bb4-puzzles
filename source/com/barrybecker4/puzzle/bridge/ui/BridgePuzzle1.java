// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.bridge.ui;

import com.barrybecker4.puzzle.bridge.Algorithm1;
import com.barrybecker4.puzzle.bridge.BridgePuzzleController1;
import com.barrybecker4.puzzle.bridge.model.Bridge1;
import com.barrybecker4.puzzle.bridge.model.BridgeMove1;
import com.barrybecker4.puzzle.common1.AlgorithmEnum;
import com.barrybecker4.puzzle.common1.PuzzleController;
import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common1.ui.DoneListener;
import com.barrybecker4.puzzle.common1.ui.NavigationPanel;
import com.barrybecker4.puzzle.common1.ui.PathNavigator;
import com.barrybecker4.puzzle.common1.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common1.ui.PuzzleViewer;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JPanel;

/**
 * This program solves a very difficult classic solitaire puzzle
 * where you select pairs of people to move across a bridge at night to get them all to the other side.
 */
public final class BridgePuzzle1 extends PuzzleApplet<Bridge1, BridgeMove1>
                             implements DoneListener {

    private NavigationPanel navPanel;

    /** Construct the applet */
    public BridgePuzzle1() {}

    /** Construct the application */
    BridgePuzzle1(String[] args) {
        super(args);
    }


    @Override
    protected PuzzleViewer<Bridge1, BridgeMove1> createViewer() {
        return new BridgeViewer1(this);
    }

    @Override
    protected PuzzleController<Bridge1, BridgeMove1> createController(Refreshable<Bridge1, BridgeMove1> viewer_) {
        return new BridgePuzzleController1(viewer_);
    }

    @Override
    protected AlgorithmEnum<Bridge1, BridgeMove1>[] getAlgorithmValues() {
        return Algorithm1.values();
    }

    protected BridgeTopControls1 createTopControls() {
        return new BridgeTopControls1(controller_, getAlgorithmValues());
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

        PuzzleApplet applet = new BridgePuzzle1(args);

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet);
    }

}

