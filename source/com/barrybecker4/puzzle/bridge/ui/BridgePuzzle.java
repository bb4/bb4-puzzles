/** Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.bridge.ui;

import com.barrybecker4.puzzle.bridge.Algorithm;
import com.barrybecker4.puzzle.bridge.BridgePuzzleController;
import com.barrybecker4.puzzle.bridge.model.Bridge;
import com.barrybecker4.puzzle.bridge.model.BridgeMove;
import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.puzzle.common.ui.DoneListener;
import com.barrybecker4.puzzle.common.ui.NavigationPanel;
import com.barrybecker4.puzzle.common.ui.PathNavigator;
import com.barrybecker4.puzzle.common.ui.PuzzleApplet;
import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JPanel;

/**
 * This program solves a very difficult classic solitaire puzzle
 * where you select pairs of people to move across a bridge at night to get them all to the other side.
 */
public final class BridgePuzzle extends PuzzleApplet<Bridge, BridgeMove>
                             implements DoneListener {

    private NavigationPanel navPanel;

    /** Construct the applet */
    public BridgePuzzle() {}

    /** Construct the application */
    public BridgePuzzle(String[] args) {
        super(args);
    }


    @Override
    protected PuzzleViewer<Bridge, BridgeMove> createViewer() {
        return new BridgeViewer(this);
    }

    @Override
    protected PuzzleController<Bridge, BridgeMove> createController(Refreshable<Bridge, BridgeMove> viewer_) {
        return new BridgePuzzleController(viewer_);
    }

    @Override
    protected AlgorithmEnum<Bridge, BridgeMove>[] getAlgorithmValues() {
        return Algorithm.values();
    }

    protected BridgeTopControls createTopControls() {
        return new BridgeTopControls(controller_, getAlgorithmValues());
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

        PuzzleApplet applet = new BridgePuzzle(args);

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet);
    }

}

