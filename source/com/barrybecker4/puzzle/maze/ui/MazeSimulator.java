/** Copyright by Barry G. Becker, 2000-2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.maze.ui;

import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.maze.MazeController;
import com.barrybecker4.ui.components.ResizableAppletPanel;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JApplet;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ComponentListener;

/**
 * A maze generator and solver application.
 * @author Barry Becker
 */
public class MazeSimulator extends JApplet {

    /** constructor */
    public MazeSimulator() {
        MathUtil.RANDOM.setSeed(1);
    }

    /**
     * Create and initialize the puzzle.
     * (init required for applet)
     */
    @Override
    public void init() {
        final MazePanel mazePanel = new MazePanel();
        MazeController controller = new MazeController(mazePanel);
        TopControlPanel topControls = new TopControlPanel(controller);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(topControls, BorderLayout.NORTH);
        panel.add(mazePanel, BorderLayout.CENTER);

        ResizableAppletPanel resizablePanel = new ResizableAppletPanel(panel);
        getContentPane().add(resizablePanel);

        ComponentListener compListener = new ResizeAdapter(mazePanel, topControls);
        getContentPane().addComponentListener(compListener);
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