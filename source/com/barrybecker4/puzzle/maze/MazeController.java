// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze;

import com.barrybecker4.common.concurrency.Worker;
import com.barrybecker4.puzzle.maze.ui.MazePanel;

import java.awt.Cursor;

/**
 * Controller part of the MVC pattern.
 * Launches generator and solvers in separate threads so the UI is not locked.
 *
 * @author Barry Becker
 */
public final class MazeController {

    private MazePanel mazePanel;
    private Worker generateWorker;
    private MazeGenerator generator;
    private MazeSolver solver;

    /**
     * Constructor.
     */
    public MazeController(MazePanel panel) {
        mazePanel = panel;
        solver = new MazeSolver(mazePanel);
    }


    /**
     * regenerate the maze based on the current UI parameter settings
     * and current size of the panel.
     */
    public void regenerate(final int thickness, final int animationSpeed,
                           final double forwardP, final double leftP, final double rightP) {

        if ((solver.isWorking())) return;
        if (generator != null)
        {
            generator.interrupt();
            // blocks until done working (which will be soon now that it has been interrupted)
            generateWorker.get();
        }

        generateWorker = new Worker() {

            @Override
            public Object construct() {
                generator = new MazeGenerator(mazePanel);
                mazePanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                double sum = forwardP + leftP + rightP;
                mazePanel.setAnimationSpeed(animationSpeed);
                mazePanel.setThickness(thickness);

                generator.generate(forwardP / sum, leftP / sum, rightP / sum);
                return true;
            }

            @Override
            public void finished() {
                mazePanel.setCursor(Cursor.getDefaultCursor());
                mazePanel.repaint();
            }
        };
        generateWorker.start();
    }


    public void solve(final int animationSpeed) {

        if (generateWorker.isWorking() || solver.isWorking()) return;

        Worker worker = new Worker() {

            @Override
            public Object construct() {

                mazePanel.setAnimationSpeed(animationSpeed);
                solver = new MazeSolver(mazePanel);
                solver.solve();
                return true;
            }

            @Override
            public void finished() {
                mazePanel.repaint();
            }
        };
        worker.start();
    }
}
