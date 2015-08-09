/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.common.ui;

import com.barrybecker4.puzzle.common.AlgorithmEnum;
import com.barrybecker4.puzzle.common.PuzzleController;
import com.barrybecker4.common.search.Refreshable;
import com.barrybecker4.ui.application.ApplicationApplet;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for Puzzle applets.
 *
 * @author Barry Becker
 */
public abstract class PuzzleApplet<P, M> extends ApplicationApplet {

    protected PuzzleController<P, M> controller_;
    protected PuzzleViewer<P, M> viewer_;

    private static final String DEFAULT_PUZZLE = "com.barrybecker4.puzzle.maze.FractalExplorer";


    /**
     * Construct the applet.
     * No argument constructor needed for applet instantiation by reflection.
     */
    public PuzzleApplet()  {
        super(new String[] {});
    }

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
        viewer_.refresh(controller_.initialState(), 0);

        TopControlPanel<P, M> topPanel = createTopControls();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(viewer_, BorderLayout.CENTER);
        JPanel bottomControls = createBottomControls();
        if (bottomControls != null) {
            mainPanel.add(bottomControls, BorderLayout.SOUTH);
        }
        return mainPanel;
    }

    protected List<String> getResourceList() {
        List<String> resources = new ArrayList<>(super.getResourceList());
        resources.add("com.barrybecker4.puzzle.common.ui.message");
        return resources;
    }

    protected abstract PuzzleViewer<P, M> createViewer();

    protected abstract PuzzleController<P, M> createController(Refreshable<P, M> viewer);

    protected TopControlPanel<P, M> createTopControls() {
        return new TopControlPanel<>(controller_, getAlgorithmValues());
    }

    protected JPanel createBottomControls() {
        return null;
    }

    protected abstract AlgorithmEnum<P, M>[] getAlgorithmValues();

}

