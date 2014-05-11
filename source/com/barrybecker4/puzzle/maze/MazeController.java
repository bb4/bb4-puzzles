// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.maze;

import com.barrybecker4.common.concurrency.Worker;
import com.barrybecker4.puzzle.maze.ui.MazePanel;
import com.barrybecker4.ui.sliders.LabeledSlider;
import com.barrybecker4.ui.sliders.SliderChangeListener;

import javax.swing.JPanel;
import java.awt.Cursor;

/**
 * Controller part of the MVC pattern.
 * Launches generator and solvers in separate threads so the UI is not locked.
 *
 * @author Barry Becker
 */
public final class MazeController implements SliderChangeListener {

    private MazePanel mazePanel;
    private Worker generateWorker;
    private MazeGenerator generator;
    private MazeSolver solver;
    private JPanel repaintListener;

    /**
     * Constructor.
     */
    public MazeController(MazePanel panel) {
        mazePanel = panel;
        solver = new MazeSolver(mazePanel);
    }

    /**
     * This panel will be repainted when the regeneration is complete.
     * Without this, the top controls do not refresh properly when shown in an applet (and only the applet).
     * @param panel the repaint listener
     */
    public void setRepaintListener(JPanel panel) {
        repaintListener = panel;
    }


    /** called when the animation speed changes */
    @Override
    public void sliderChanged(LabeledSlider slider) {
        mazePanel.setAnimationSpeed((int)slider.getValue());
    }

    /**
     * regenerate the maze based on the current UI parameter settings
     * and current size of the panel.
     */
    public void regenerate(final int thickness, final int animationSpeed,
                           final double forwardP, final double leftP, final double rightP) {

        if (solver.isWorking()) {
            solver.interrupt();
        }

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
                if (repaintListener != null)  {
                    repaintListener.repaint();
                }
            }
        };
        generateWorker.start();
    }


    /**
     * Don't solve if already generating or solving.
     * @param animationSpeed the speed at which to show the solution.
     */
    public void solve(final int animationSpeed) {

        if (generateWorker.isWorking()) return;
        if (solver.isWorking()) {
            solver.interrupt();
        }

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
