// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.sudoku.ui;

import com.barrybecker4.puzzle.sudoku.Data;
import com.barrybecker4.puzzle.sudoku.SudokuController;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

/**
 * Sudoku Puzzle UI.
 * This program can generate and solve Sudoku puzzles.
 *
 * @author Barry Becker
 */
public final class SudokuPuzzle extends JApplet {

    /**
     * Construct the application and set the look and feel.
     */
    public SudokuPuzzle() {
        GUIUtil.setCustomLookAndFeel();
    }

    /**
     * Create and initialize the puzzle.
     * (init required for applet)
     */
    @Override
    public void init() {
        SudokuPanel puzzlePanel = new SudokuPanel(Data.SIMPLE_9);
        SudokuController controller = new SudokuController(puzzlePanel);
        TopControlPanel topControls = new TopControlPanel(controller);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(topControls, BorderLayout.NORTH);
        panel.add(puzzlePanel, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    /**
     * Called by the browser after init(), if running as an applet.
     */
    @Override
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getContentPane().repaint();
            }
        });
    }

    @Override
    public String getName() {
        return "Sudoku Puzzle Solver";
    }

    /**
     * use this to run as an application instead of an applet.
     */
    public static void main( String[] args )  {

        SudokuPuzzle applet = new SudokuPuzzle();

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet( applet);
    }
}
