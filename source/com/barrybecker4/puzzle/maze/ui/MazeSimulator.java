// Copyright by Barry G. Becker, 2000-2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze.ui;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.maze.MazeController;
import com.barrybecker4.ui.application.ApplicationApplet;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;

/**
 * A maze generator and solver application.
 * @author Barry Becker
 */
public class MazeSimulator extends ApplicationApplet {

    /** constructor */
    public MazeSimulator() {
        MathUtil.RANDOM.setSeed(1);
    }

    /**
     * Create and initialize the puzzle.
     * (init required for applet)
     */
    @Override
    public JPanel createMainPanel() {
        final MazePanel mazePanel = new MazePanel();
        MazeController controller = new MazeController(mazePanel);
        TopControlPanel topControls = new TopControlPanel(controller);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(topControls, BorderLayout.NORTH);
        panel.add(mazePanel, BorderLayout.CENTER);

        ComponentListener compListener = new ResizeAdapter(mazePanel, topControls);
        panel.addComponentListener(compListener);
        return panel;
    }

    @Override
    public String getName() {
        return "Maze Generator";
    }


    //------ Main method --------------------------------------------------------

    public static void main( String[] args ) {
        MazeSimulator simulator = new MazeSimulator();
        GUIUtil.showApplet( simulator);
    }
}