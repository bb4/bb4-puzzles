/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.puzzle.common.Refreshable;
import com.barrybecker4.ui.application.ApplicationApplet;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Base class for Puzzle applets.
 *
 * @author Barry Becker
 */
public abstract class PuzzleApplet<P, M> extends ApplicationApplet {

    protected PuzzleController<P, M> controller_;
    protected PuzzleViewer<P, M> viewer_;


    /**
     * Construct the applet.
     * No argument constructor needed for applet instantiation by reflection.
     */
    public PuzzleApplet() {}

    /**
     * Construct the application.
     */
    public PuzzleApplet(String[] args) {
        super(args);
    }

    /**
     * create and initialize the puzzle
     * (init required for applet)
     */
    @Override
    protected JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        viewer_ = createViewer();
        controller_ = createController(viewer_);
        viewer_.refresh(controller_.initialPosition(), 0);


        TopControlPanel<P, M> topPanel =
            new TopControlPanel<P, M>(controller_, getAlgorithmValues());

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(viewer_, BorderLayout.CENTER);
        JPanel customControls = createCustomControls();
        if (customControls != null) {
            mainPanel.add(customControls, BorderLayout.SOUTH);
        }
        return mainPanel;
    }

    protected abstract PuzzleViewer<P, M> createViewer();

    protected abstract PuzzleController<P, M> createController(Refreshable<P, M> viewer);

    protected JPanel createCustomControls() {
        return null;
    }

    protected abstract AlgorithmEnum<P, M>[] getAlgorithmValues();
}

